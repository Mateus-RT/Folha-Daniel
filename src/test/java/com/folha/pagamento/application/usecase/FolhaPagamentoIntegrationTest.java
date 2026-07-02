package com.folha.pagamento.application.usecase;

import com.folha.pagamento.domain.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
    "spring.shell.interactive.enabled=false",
    "spring.shell.script.enabled=false",
    "app.cli.enabled=false",
    "spring.flyway.enabled=true"
})
@Transactional
public class FolhaPagamentoIntegrationTest {

    @Autowired
    private ManterFuncionarioUseCase manterFuncionarioUseCase;

    @Autowired
    private CalcularFolhaUseCase calcularFolhaUseCase;

    @Autowired
    private ConsultarHistoricoUseCase consultarHistoricoUseCase;

    private String gerarCpfValido() {
        Random random = new Random();
        int[] noveDigitos = new int[9];
        // Evita gerar todos os dígitos iguais
        while (true) {
            for (int i = 0; i < 9; i++) {
                noveDigitos[i] = random.nextInt(10);
            }
            boolean todosIguais = true;
            for (int i = 1; i < 9; i++) {
                if (noveDigitos[i] != noveDigitos[0]) {
                    todosIguais = false;
                    break;
                }
            }
            if (!todosIguais) break;
        }

        // Primeiro dígito verificador
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += noveDigitos[i] * (10 - i);
        }
        int d1 = (soma * 10) % 11;
        if (d1 >= 10) d1 = 0;

        // Segundo dígito verificador
        soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += noveDigitos[i] * (11 - i);
        }
        soma += d1 * 2;
        int d2 = (soma * 10) % 11;
        if (d2 >= 10) d2 = 0;

        StringBuilder sb = new StringBuilder();
        for (int digito : noveDigitos) {
            sb.append(digito);
        }
        sb.append(d1).append(d2);
        return sb.toString();
    }

    @Test
    public void testFluxoCompletoClt() {
        String cpf = gerarCpfValido();

        // 1. Cadastrar funcionário
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Integracao CLT Silva");
        funcionario.setCpf(cpf);
        funcionario.setRegimeTrabalho(RegimeTrabalho.CLT);
        funcionario.setSalarioBase(new BigDecimal("5000.00"));
        funcionario.setHoraExtra(new BigDecimal("300.00"));
        funcionario.setAdicionalNoturno(new BigDecimal("150.00"));
        funcionario.setQuantidadeDependentes(0);
        funcionario.setCargo("Engenheiro");
        funcionario.setStatus(StatusFuncionario.ATIVO);
        funcionario.setDataAdmissao(LocalDate.now());

        Funcionario cadastrado = manterFuncionarioUseCase.cadastrar(funcionario);
        assertNotNull(cadastrado.getId());

        // 2. Calcular folha (05/2025)
        CalculoResultado resultado = calcularFolhaUseCase.calcularIndividual(cadastrado.getCpf(), 5, 2025);
        assertNotNull(resultado.getId());
        assertEquals(new BigDecimal("5450.00"), resultado.getTotalVencimentos());
        assertEquals(new BigDecimal("4432.12"), resultado.getSalarioLiquido());

        // 3. Consultar histórico
        List<CalculoResultado> historico = consultarHistoricoUseCase.buscarPorCpf(cadastrado.getCpf());
        assertEquals(1, historico.size());
        assertEquals(new BigDecimal("4432.12"), historico.get(0).getSalarioLiquido());
        assertEquals("Engenheiro", historico.get(0).getCargo());
    }

    @Test
    public void testFluxoLote() {
        String cpf1 = gerarCpfValido();
        String cpf2 = gerarCpfValido();

        // Cadastrar dois funcionários ativos
        Funcionario f1 = new Funcionario();
        f1.setNome("Lote Um");
        f1.setCpf(cpf1);
        f1.setRegimeTrabalho(RegimeTrabalho.CLT);
        f1.setSalarioBase(new BigDecimal("2000.00"));
        f1.setCargo("Auxiliar");
        f1.setStatus(StatusFuncionario.ATIVO);
        f1.setDataAdmissao(LocalDate.now());
        manterFuncionarioUseCase.cadastrar(f1);

        Funcionario f2 = new Funcionario();
        f2.setNome("Lote Dois");
        f2.setCpf(cpf2);
        f2.setRegimeTrabalho(RegimeTrabalho.JOVEM_APRENDIZ);
        f2.setSalarioBase(new BigDecimal("1000.00"));
        f2.setCargo("Aprendiz");
        f2.setStatus(StatusFuncionario.ATIVO);
        f2.setDataAdmissao(LocalDate.now());
        manterFuncionarioUseCase.cadastrar(f2);

        // Calcular em lote
        List<CalculoResultado> calculados = calcularFolhaUseCase.calcularEmLote(5, 2025);
        assertTrue(calculados.size() >= 2);
    }

    @Test
    public void testCalculoComissionadoComComissaoTransiente() {
        String cpf = gerarCpfValido();

        // 1. Cadastrar funcionário comissionado
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Comissionado Silva");
        funcionario.setCpf(cpf);
        funcionario.setRegimeTrabalho(RegimeTrabalho.COMISSIONADO);
        funcionario.setSalarioBase(new BigDecimal("3000.00"));
        funcionario.setQuantidadeDependentes(0);
        funcionario.setCargo("Vendedor");
        funcionario.setStatus(StatusFuncionario.ATIVO);
        funcionario.setDataAdmissao(LocalDate.now());

        Funcionario cadastrado = manterFuncionarioUseCase.cadastrar(funcionario);
        assertNotNull(cadastrado.getId());

        // 2. Calcular folha passando comissão transiente (ex: 1500.00) para 05/2025
        BigDecimal comissaoTransiente = new BigDecimal("1500.00");
        CalculoResultado resultado = calcularFolhaUseCase.calcularIndividual(
                cadastrado.getCpf(), 5, 2025, comissaoTransiente, null);

        assertNotNull(resultado.getId());
        // Total de vencimentos deve ser salarioBase + comissão = 4500.00
        assertEquals(new BigDecimal("4500.00"), resultado.getTotalVencimentos());

        // Verificar se foi salvo corretamente no histórico e os valores batem
        List<CalculoResultado> historico = consultarHistoricoUseCase.buscarPorCpf(cadastrado.getCpf());
        assertEquals(1, historico.size());
        CalculoResultado salvo = historico.get(0);
        assertEquals(new BigDecimal("4500.00"), salvo.getTotalVencimentos());
        
        // Verificar se a verba de comissão está presente
        boolean temComissao = salvo.getVerbas().stream()
                .anyMatch(v -> v.getNome().equals("Comissões") && v.getValor().compareTo(comissaoTransiente) == 0);
        assertTrue(temComissao);
    }

    @Test
    public void testCalculoPjComIssTransiente() {
        String cpf = gerarCpfValido();

        // 1. Cadastrar funcionário PJ com ISS de 5%
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("PJ Tech");
        funcionario.setCpf(cpf);
        funcionario.setRegimeTrabalho(RegimeTrabalho.PJ);
        funcionario.setSalarioBase(new BigDecimal("10000.00"));
        funcionario.setAliquotaIssPj(new BigDecimal("5.00"));
        funcionario.setCargo("Consultor");
        funcionario.setStatus(StatusFuncionario.ATIVO);
        funcionario.setDataAdmissao(LocalDate.now());

        Funcionario cadastrado = manterFuncionarioUseCase.cadastrar(funcionario);
        assertNotNull(cadastrado.getId());

        // 2. Calcular folha aplicando ISS transiente = true
        CalculoResultado resultado = calcularFolhaUseCase.calcularIndividual(
                cadastrado.getCpf(), 5, 2025, null, true);

        assertNotNull(resultado.getId());
        // Total de vencimentos = 10000.00
        assertEquals(new BigDecimal("10000.00"), resultado.getTotalVencimentos());
        // Dedução ISS = 10000 * 0.05 = 500.00
        // Salário Líquido = 9500.00
        assertEquals(new BigDecimal("500.00"), resultado.getTotalDescontos());
        assertEquals(new BigDecimal("9500.00"), resultado.getSalarioLiquido());

        // Verificar no histórico
        List<CalculoResultado> historico = consultarHistoricoUseCase.buscarPorCpf(cadastrado.getCpf());
        assertEquals(1, historico.size());
        CalculoResultado salvo = historico.get(0);
        assertEquals(new BigDecimal("9500.00"), salvo.getSalarioLiquido());

        // Verificar se a verba de dedução de ISS está presente
        boolean temIss = salvo.getVerbas().stream()
                .anyMatch(v -> v.getNome().equals("Dedução ISS") && v.getValor().compareTo(new BigDecimal("500.00")) == 0);
        assertTrue(temIss);
    }

    @Test
    public void testInativarFuncionario() {
        String cpf = gerarCpfValido();

        // 1. Cadastrar funcionário CLT ativo
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Inativo Silva");
        funcionario.setCpf(cpf);
        funcionario.setRegimeTrabalho(RegimeTrabalho.CLT);
        funcionario.setSalarioBase(new BigDecimal("2000.00"));
        funcionario.setCargo("Analista");
        funcionario.setStatus(StatusFuncionario.ATIVO);
        funcionario.setDataAdmissao(LocalDate.now());

        Funcionario cadastrado = manterFuncionarioUseCase.cadastrar(funcionario);
        assertEquals(StatusFuncionario.ATIVO, cadastrado.getStatus());

        // 2. Inativar funcionário no use case
        Funcionario inativado = manterFuncionarioUseCase.inativar(cpf);
        assertEquals(StatusFuncionario.INATIVO, inativado.getStatus());
        assertNotNull(inativado.getDataRescisao());

        // 3. Buscar no use case / banco de dados por CPF e validar que está inativo
        Funcionario buscado = manterFuncionarioUseCase.buscarPorCpf(cpf)
                .orElseThrow(() -> new AssertionError("Funcionário deveria existir"));
        assertEquals(StatusFuncionario.INATIVO, buscado.getStatus());
    }

    @Test
    public void testAlterarFuncionarioComSalarioNuloDeveLancarExcecao() {
        String cpf = gerarCpfValido();
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Teste Alterar");
        funcionario.setCpf(cpf);
        funcionario.setRegimeTrabalho(RegimeTrabalho.PJ);
        funcionario.setSalarioBase(new BigDecimal("3000.00"));
        funcionario.setCargo("Programador");
        funcionario.setStatus(StatusFuncionario.ATIVO);
        funcionario.setDataAdmissao(LocalDate.now());

        manterFuncionarioUseCase.cadastrar(funcionario);

        IllegalArgumentException exceptionNull = assertThrows(IllegalArgumentException.class, () -> {
            manterFuncionarioUseCase.alterar(cpf, null, "Analista");
        });
        assertEquals("Erro: O salário base deve ser maior que zero.", exceptionNull.getMessage());
    }

    @Test
    public void testAlterarFuncionarioComSalarioZeroOuNegativoDeveLancarExcecao() {
        String cpf = gerarCpfValido();
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Teste Alterar Dois");
        funcionario.setCpf(cpf);
        funcionario.setRegimeTrabalho(RegimeTrabalho.PJ);
        funcionario.setSalarioBase(new BigDecimal("3000.00"));
        funcionario.setCargo("Programador");
        funcionario.setStatus(StatusFuncionario.ATIVO);
        funcionario.setDataAdmissao(LocalDate.now());

        manterFuncionarioUseCase.cadastrar(funcionario);

        IllegalArgumentException exceptionZero = assertThrows(IllegalArgumentException.class, () -> {
            manterFuncionarioUseCase.alterar(cpf, BigDecimal.ZERO, "Analista");
        });
        assertEquals("Erro: O salário base deve ser maior que zero.", exceptionZero.getMessage());

        IllegalArgumentException exceptionNegativo = assertThrows(IllegalArgumentException.class, () -> {
            manterFuncionarioUseCase.alterar(cpf, new BigDecimal("-100.00"), "Analista");
        });
        assertEquals("Erro: O salário base deve ser maior que zero.", exceptionNegativo.getMessage());
    }
}
