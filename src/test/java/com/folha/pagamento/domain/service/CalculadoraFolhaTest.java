package com.folha.pagamento.domain.service;

import com.folha.pagamento.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CalculadoraFolhaTest {

    private CalculadoraFolha calculadora;
    private ParametrosLegislacao parametros2025;

    @BeforeEach
    public void setUp() {
        calculadora = new CalculadoraFolhaServiceImpl();

        // Configuração de Parâmetros de Legislação de 2025 para testes
        List<FaixaInss> faixasInss = new ArrayList<>();
        faixasInss.add(new FaixaInss(1L, 1, BigDecimal.ZERO, new BigDecimal("1518.00"), new BigDecimal("7.50")));
        faixasInss.add(new FaixaInss(2L, 2, new BigDecimal("1518.01"), new BigDecimal("2793.88"), new BigDecimal("9.00")));
        faixasInss.add(new FaixaInss(3L, 3, new BigDecimal("2793.89"), new BigDecimal("4190.83"), new BigDecimal("12.00")));
        faixasInss.add(new FaixaInss(4L, 4, new BigDecimal("4190.84"), new BigDecimal("8157.41"), new BigDecimal("14.00")));

        List<FaixaIrrf> faixasIrrf = new ArrayList<>();
        faixasIrrf.add(new FaixaIrrf(1L, 1, BigDecimal.ZERO, new BigDecimal("2259.20"), BigDecimal.ZERO, BigDecimal.ZERO));
        faixasIrrf.add(new FaixaIrrf(2L, 2, new BigDecimal("2259.21"), new BigDecimal("2826.65"), new BigDecimal("7.50"), new BigDecimal("169.44")));
        faixasIrrf.add(new FaixaIrrf(3L, 3, new BigDecimal("2826.66"), new BigDecimal("3751.05"), new BigDecimal("15.00"), new BigDecimal("381.44")));
        faixasIrrf.add(new FaixaIrrf(4L, 4, new BigDecimal("3751.06"), new BigDecimal("4664.68"), new BigDecimal("22.50"), new BigDecimal("662.77")));
        faixasIrrf.add(new FaixaIrrf(5L, 5, new BigDecimal("4664.69"), null, new BigDecimal("27.50"), new BigDecimal("896.00")));

        parametros2025 = new ParametrosLegislacao(
                1L, 2025, 1,
                new BigDecimal("1518.00"),
                new BigDecimal("8157.41"),
                new BigDecimal("189.59"),
                new BigDecimal("564.80"),
                new BigDecimal("8.00"),
                new BigDecimal("2.00"),
                new BigDecimal("5.00"),
                faixasInss,
                faixasIrrf
        );
    }

    @Test
    public void testCltCalculation() {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(10L);
        funcionario.setNome("CLT Joao Silva");
        funcionario.setCpf("11111111111");
        funcionario.setRegimeTrabalho(RegimeTrabalho.CLT);
        funcionario.setSalarioBase(new BigDecimal("5000.00"));
        funcionario.setHoraExtra(new BigDecimal("300.00"));
        funcionario.setAdicionalNoturno(new BigDecimal("150.00"));
        funcionario.setQuantidadeDependentes(0);
        funcionario.setCargo("Analista");

        CalculoResultado res = calculadora.calcular(funcionario, 5, 2025, parametros2025);

        assertNotNull(res);
        assertEquals(new BigDecimal("5450.00"), res.getTotalVencimentos());
        assertEquals(new BigDecimal("572.59"), getVerbaValor(res, "INSS"));
        assertEquals(new BigDecimal("445.29"), getVerbaValor(res, "IRRF"));
        assertEquals(new BigDecimal("436.00"), res.getValorFgts());
        assertEquals(new BigDecimal("4432.12"), res.getSalarioLiquido());
    }

    @Test
    public void testPjCalculationWithIss() {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(11L);
        funcionario.setRegimeTrabalho(RegimeTrabalho.PJ);
        funcionario.setSalarioBase(new BigDecimal("12000.00"));
        funcionario.setAliquotaIssPj(new BigDecimal("5.00"));
        funcionario.setAplicarIssCalculo(true);

        CalculoResultado res = calculadora.calcular(funcionario, 5, 2025, parametros2025);

        assertNotNull(res);
        assertEquals(new BigDecimal("12000.00"), res.getTotalVencimentos());
        assertEquals(new BigDecimal("600.00"), getVerbaValor(res, "Dedução ISS"));
        assertEquals(new BigDecimal("11400.00"), res.getSalarioLiquido());
    }

    @Test
    public void testPjCalculationWithoutIss() {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(11L);
        funcionario.setRegimeTrabalho(RegimeTrabalho.PJ);
        funcionario.setSalarioBase(new BigDecimal("12000.00"));
        funcionario.setAliquotaIssPj(new BigDecimal("5.00"));
        funcionario.setAplicarIssCalculo(false);

        CalculoResultado res = calculadora.calcular(funcionario, 6, 2025, parametros2025);

        assertNotNull(res);
        assertEquals(new BigDecimal("12000.00"), res.getTotalVencimentos());
        assertNull(getVerbaValor(res, "Dedução ISS"));
        assertEquals(new BigDecimal("12000.00"), res.getSalarioLiquido());
    }

    @Test
    public void testComissionadoCalculation() {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(12L);
        funcionario.setRegimeTrabalho(RegimeTrabalho.COMISSIONADO);
        funcionario.setSalarioBase(new BigDecimal("2000.00"));
        funcionario.setComissaoMensal(new BigDecimal("3000.00"));
        funcionario.setQuantidadeDependentes(0);

        CalculoResultado res = calculadora.calcular(funcionario, 5, 2025, parametros2025);

        assertNotNull(res);
        assertEquals(new BigDecimal("5000.00"), res.getTotalVencimentos());
        assertEquals(new BigDecimal("509.59"), getVerbaValor(res, "INSS"));
        assertEquals(new BigDecimal("335.15"), getVerbaValor(res, "IRRF")); // Desconto simplificado é mais favorável (335.15) que convencional (347.57)
        assertEquals(new BigDecimal("400.00"), res.getValorFgts());
        assertEquals(new BigDecimal("4155.26"), res.getSalarioLiquido()); // 5000.00 - 509.59 - 335.15 = 4155.26
    }

    @Test
    public void testEstagioCalculation() {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(13L);
        funcionario.setRegimeTrabalho(RegimeTrabalho.ESTAGIO);
        funcionario.setSalarioBase(new BigDecimal("1500.00"));
        funcionario.setAuxilioTransporte(new BigDecimal("200.00"));

        CalculoResultado res = calculadora.calcular(funcionario, 5, 2025, parametros2025);

        assertNotNull(res);
        assertEquals(new BigDecimal("1500.00"), res.getTotalVencimentos());
        assertEquals(new BigDecimal("1700.00"), res.getSalarioLiquido());
        assertEquals(0, res.getValorFgts().compareTo(BigDecimal.ZERO));
    }

    @Test
    public void testJovemAprendizCalculation() {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(14L);
        funcionario.setRegimeTrabalho(RegimeTrabalho.JOVEM_APRENDIZ);
        funcionario.setSalarioBase(new BigDecimal("1000.00"));

        CalculoResultado res = calculadora.calcular(funcionario, 5, 2025, parametros2025);

        assertNotNull(res);
        assertEquals(new BigDecimal("1000.00"), res.getTotalVencimentos());
        assertEquals(new BigDecimal("50.00"), getVerbaValor(res, "INSS Aprendiz"));
        assertEquals(new BigDecimal("20.00"), res.getValorFgts());
        assertEquals(new BigDecimal("950.00"), res.getSalarioLiquido());
    }

    @Test
    public void testRpaCalculation() {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(15L);
        funcionario.setRegimeTrabalho(RegimeTrabalho.RPA);
        funcionario.setSalarioBase(new BigDecimal("3000.00"));
        funcionario.setTipoInssRpa("ONZE_PORCENTO");

        CalculoResultado res = calculadora.calcular(funcionario, 5, 2025, parametros2025);

        assertNotNull(res);
        assertEquals(new BigDecimal("3000.00"), res.getTotalVencimentos());
        assertEquals(new BigDecimal("330.00"), getVerbaValor(res, "INSS Autônomo"));
        assertEquals(new BigDecimal("30.81"), getVerbaValor(res, "IRRF")); // Apenas IRRF convencional para RPA: (3000 - 330) * 7.5% - 169.44 = 30.81
        assertEquals(new BigDecimal("2639.19"), res.getSalarioLiquido()); // 3000 - 330 - 30.81 = 2639.19
    }

    private BigDecimal getVerbaValor(CalculoResultado res, String nome) {
        return res.getVerbas().stream()
                .filter(v -> v.getNome().equals(nome))
                .map(VerbaCalculada::getValor)
                .findFirst()
                .orElse(null);
    }
}
