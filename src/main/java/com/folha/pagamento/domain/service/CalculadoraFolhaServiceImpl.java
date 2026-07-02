package com.folha.pagamento.domain.service;

import com.folha.pagamento.domain.model.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalculadoraFolhaServiceImpl implements CalculadoraFolha {

    @Override
    public CalculoResultado calcular(Funcionario funcionario, int mes, int ano, ParametrosLegislacao parametros) {
        BigDecimal salarioBase = funcionario.getSalarioBase() != null ? funcionario.getSalarioBase() : BigDecimal.ZERO;
        BigDecimal totalVencimentos = BigDecimal.ZERO;
        BigDecimal totalDescontos = BigDecimal.ZERO;
        BigDecimal salarioLiquido = BigDecimal.ZERO;
        BigDecimal valorFgts = BigDecimal.ZERO;
        List<VerbaCalculada> verbas = new ArrayList<>();

        RegimeTrabalho regime = funcionario.getRegimeTrabalho();

        if (regime == RegimeTrabalho.CLT) {
            BigDecimal horaExtra = funcionario.getHoraExtra() != null ? funcionario.getHoraExtra() : BigDecimal.ZERO;
            BigDecimal adicionalNoturno = funcionario.getAdicionalNoturno() != null ? funcionario.getAdicionalNoturno() : BigDecimal.ZERO;
            
            totalVencimentos = salarioBase.add(horaExtra).add(adicionalNoturno);
            
            BigDecimal inss = calcularInssProgressivo(totalVencimentos, parametros);
            BigDecimal irrf = calcularIrrfProgressivo(totalVencimentos, inss, funcionario.getQuantidadeDependentes(), parametros, regime);
            
            BigDecimal aliquotaFgts = parametros.getAliquotaFgtsClt().divide(new BigDecimal("100.0"), 4, RoundingMode.HALF_UP);
            valorFgts = totalVencimentos.multiply(aliquotaFgts).setScale(2, RoundingMode.HALF_UP);
            
            totalDescontos = inss.add(irrf);
            salarioLiquido = totalVencimentos.subtract(totalDescontos);

            verbas.add(new VerbaCalculada("Salário Base", TipoVerba.PROVENTO, salarioBase, "Referência"));
            if (horaExtra.compareTo(BigDecimal.ZERO) > 0) {
                verbas.add(new VerbaCalculada("Hora Extra", TipoVerba.PROVENTO, horaExtra, "Adicional"));
            }
            if (adicionalNoturno.compareTo(BigDecimal.ZERO) > 0) {
                verbas.add(new VerbaCalculada("Adicional Noturno", TipoVerba.PROVENTO, adicionalNoturno, "Adicional"));
            }
            verbas.add(new VerbaCalculada("INSS", TipoVerba.DESCONTO, inss, "Desconto Progressivo"));
            verbas.add(new VerbaCalculada("IRRF", TipoVerba.DESCONTO, irrf, "Desconto Progressivo"));
            verbas.add(new VerbaCalculada("FGTS", TipoVerba.INFORMATIVA, valorFgts, "Encargo Empresa (8%)"));

        } else if (regime == RegimeTrabalho.PJ) {
            totalVencimentos = salarioBase;
            BigDecimal deducoes = BigDecimal.ZERO;

            verbas.add(new VerbaCalculada("Valor do Contrato", TipoVerba.PROVENTO, salarioBase, "Referência"));

            if (Boolean.TRUE.equals(funcionario.getAplicarIssCalculo()) && funcionario.getAliquotaIssPj() != null) {
                BigDecimal aliquotaIss = funcionario.getAliquotaIssPj().divide(new BigDecimal("100.0"), 4, RoundingMode.HALF_UP);
                deducoes = totalVencimentos.multiply(aliquotaIss).setScale(2, RoundingMode.HALF_UP);
                verbas.add(new VerbaCalculada("Dedução ISS", TipoVerba.DESCONTO, deducoes, "Alíquota " + funcionario.getAliquotaIssPj() + "%"));
            }

            totalDescontos = deducoes;
            salarioLiquido = totalVencimentos.subtract(totalDescontos);

        } else if (regime == RegimeTrabalho.COMISSIONADO) {
            BigDecimal comissao = funcionario.getComissaoMensal() != null ? funcionario.getComissaoMensal() : BigDecimal.ZERO;
            totalVencimentos = salarioBase.add(comissao);

            BigDecimal inss = calcularInssProgressivo(totalVencimentos, parametros);
            BigDecimal irrf = calcularIrrfProgressivo(totalVencimentos, inss, funcionario.getQuantidadeDependentes(), parametros, regime);
            
            BigDecimal aliquotaFgts = parametros.getAliquotaFgtsClt().divide(new BigDecimal("100.0"), 4, RoundingMode.HALF_UP);
            valorFgts = totalVencimentos.multiply(aliquotaFgts).setScale(2, RoundingMode.HALF_UP);
            
            totalDescontos = inss.add(irrf);
            salarioLiquido = totalVencimentos.subtract(totalDescontos);

            verbas.add(new VerbaCalculada("Salário Fixo", TipoVerba.PROVENTO, salarioBase, "Referência"));
            if (comissao.compareTo(BigDecimal.ZERO) > 0) {
                verbas.add(new VerbaCalculada("Comissões", TipoVerba.PROVENTO, comissao, "Comissões do mês"));
            }
            verbas.add(new VerbaCalculada("INSS", TipoVerba.DESCONTO, inss, "Desconto Progressivo"));
            verbas.add(new VerbaCalculada("IRRF", TipoVerba.DESCONTO, irrf, "Desconto Progressivo"));
            verbas.add(new VerbaCalculada("FGTS", TipoVerba.INFORMATIVA, valorFgts, "Encargo Empresa (8%)"));

        } else if (regime == RegimeTrabalho.ESTAGIO) {
            totalVencimentos = salarioBase;
            BigDecimal transporte = funcionario.getAuxilioTransporte() != null ? funcionario.getAuxilioTransporte() : BigDecimal.ZERO;
            
            BigDecimal irrf = calcularIrrfProgressivo(totalVencimentos, BigDecimal.ZERO, 0, parametros, regime);
            
            totalDescontos = irrf;
            salarioLiquido = totalVencimentos.add(transporte).subtract(totalDescontos);

            verbas.add(new VerbaCalculada("Bolsa Auxílio", TipoVerba.PROVENTO, salarioBase, "Referência"));
            if (transporte.compareTo(BigDecimal.ZERO) > 0) {
                verbas.add(new VerbaCalculada("Auxílio Transporte", TipoVerba.PROVENTO, transporte, "Auxílio"));
            }
            if (irrf.compareTo(BigDecimal.ZERO) > 0) {
                verbas.add(new VerbaCalculada("IRRF", TipoVerba.DESCONTO, irrf, "Desconto Progressivo"));
            }

        } else if (regime == RegimeTrabalho.JOVEM_APRENDIZ) {
            totalVencimentos = salarioBase;
            
            BigDecimal inss = totalVencimentos.multiply(new BigDecimal("0.05")).setScale(2, RoundingMode.HALF_UP);
            BigDecimal irrf = calcularIrrfProgressivo(totalVencimentos.subtract(inss), BigDecimal.ZERO, 0, parametros, regime);
            
            BigDecimal aliquotaFgts = parametros.getAliquotaFgtsAprendiz().divide(new BigDecimal("100.0"), 4, RoundingMode.HALF_UP);
            valorFgts = totalVencimentos.multiply(aliquotaFgts).setScale(2, RoundingMode.HALF_UP);
            
            totalDescontos = inss.add(irrf);
            salarioLiquido = totalVencimentos.subtract(totalDescontos);

            verbas.add(new VerbaCalculada("Salário Proporcional", TipoVerba.PROVENTO, salarioBase, "Referência"));
            verbas.add(new VerbaCalculada("INSS Aprendiz", TipoVerba.DESCONTO, inss, "Alíquota 5%"));
            if (irrf.compareTo(BigDecimal.ZERO) > 0) {
                verbas.add(new VerbaCalculada("IRRF", TipoVerba.DESCONTO, irrf, "Desconto Progressivo"));
            }
            verbas.add(new VerbaCalculada("FGTS Aprendiz", TipoVerba.INFORMATIVA, valorFgts, "Encargo Empresa (2%)"));

        } else if (regime == RegimeTrabalho.RPA) {
            totalVencimentos = salarioBase;
            BigDecimal aliquotaInss = "ONZE_PORCENTO".equals(funcionario.getTipoInssRpa()) ? new BigDecimal("11.0") : new BigDecimal("20.0");
            BigDecimal baseInss = totalVencimentos.min(parametros.getTetoInss());
            BigDecimal inss = baseInss.multiply(aliquotaInss.divide(new BigDecimal("100.0"))).setScale(2, RoundingMode.HALF_UP);

            BigDecimal baseIrrf = totalVencimentos.subtract(inss);
            BigDecimal irrf = calcularIrrfProgressivo(baseIrrf, BigDecimal.ZERO, 0, parametros, regime);

            totalDescontos = inss.add(irrf);
            salarioLiquido = totalVencimentos.subtract(totalDescontos);

            verbas.add(new VerbaCalculada("Serviço Prestado (RPA)", TipoVerba.PROVENTO, salarioBase, "Referência"));
            verbas.add(new VerbaCalculada("INSS Autônomo", TipoVerba.DESCONTO, inss, "Alíquota " + aliquotaInss + "%"));
            if (irrf.compareTo(BigDecimal.ZERO) > 0) {
                verbas.add(new VerbaCalculada("IRRF", TipoVerba.DESCONTO, irrf, "Desconto Progressivo"));
            }
        }

        return new CalculoResultado(
                null,
                funcionario.getId(),
                mes,
                ano,
                regime,
                salarioBase,
                totalVencimentos.setScale(2, RoundingMode.HALF_UP),
                totalDescontos.setScale(2, RoundingMode.HALF_UP),
                salarioLiquido.setScale(2, RoundingMode.HALF_UP),
                valorFgts.setScale(2, RoundingMode.HALF_UP),
                funcionario.getCargo(),
                verbas
        );
    }

    private BigDecimal calcularInssProgressivo(BigDecimal bruto, ParametrosLegislacao parametros) {
        BigDecimal baseCalculo = bruto.min(parametros.getTetoInss());
        BigDecimal inssTotal = BigDecimal.ZERO;
        BigDecimal limiteAnterior = BigDecimal.ZERO;

        List<FaixaInss> faixas = new ArrayList<>(parametros.getFaixasInss());
        faixas.sort((f1, f2) -> f1.getFaixaOrdem().compareTo(f2.getFaixaOrdem()));

        for (FaixaInss faixa : faixas) {
            BigDecimal limiteMinimo = faixa.getLimiteMinimo();
            BigDecimal limiteMaximo = faixa.getLimiteMaximo();
            BigDecimal aliquota = faixa.getAliquota().divide(new BigDecimal("100.0"), 4, RoundingMode.HALF_UP);

            if (baseCalculo.compareTo(limiteMinimo) > 0) {
                BigDecimal limiteEfetivo = (limiteMaximo == null || baseCalculo.compareTo(limiteMaximo) < 0) 
                        ? baseCalculo 
                        : limiteMaximo;
                BigDecimal faixaBase = limiteEfetivo.subtract(limiteAnterior);
                BigDecimal impostoFaixa = faixaBase.multiply(aliquota).setScale(2, RoundingMode.HALF_UP);
                inssTotal = inssTotal.add(impostoFaixa);
                limiteAnterior = limiteMaximo;
            } else {
                break;
            }
        }

        if (baseCalculo.compareTo(parametros.getTetoInss()) >= 0) {
            BigDecimal somaTeto = BigDecimal.ZERO;
            BigDecimal anterior = BigDecimal.ZERO;
            for (FaixaInss faixa : faixas) {
                BigDecimal limiteMax = faixa.getLimiteMaximo();
                BigDecimal aliquota = faixa.getAliquota().divide(new BigDecimal("100.0"), 4, RoundingMode.HALF_UP);
                if (limiteMax != null) {
                    BigDecimal faixaBase = limiteMax.subtract(anterior);
                    somaTeto = somaTeto.add(faixaBase.multiply(aliquota).setScale(2, RoundingMode.HALF_UP));
                    anterior = limiteMax;
                } else {
                    BigDecimal faixaBase = parametros.getTetoInss().subtract(anterior);
                    somaTeto = somaTeto.add(faixaBase.multiply(aliquota).setScale(2, RoundingMode.HALF_UP));
                }
            }
            return somaTeto.setScale(2, RoundingMode.HALF_UP);
        }

        return inssTotal.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcularIrrfProgressivo(BigDecimal bruto, BigDecimal inss, int dependentes, ParametrosLegislacao parametros, RegimeTrabalho regime) {
        BigDecimal deducaoDependentes = BigDecimal.valueOf(dependentes).multiply(parametros.getValorDependenteIrrf());
        BigDecimal baseConvencional = bruto.subtract(inss).subtract(deducaoDependentes);
        BigDecimal irrfConvencional = calcularIrrfBase(baseConvencional, parametros);

        if (regime == RegimeTrabalho.CLT || regime == RegimeTrabalho.COMISSIONADO) {
            BigDecimal irrfSimplificado = new BigDecimal("999999.99");
            if (parametros.getDescontoSimplificadoIrrf() != null && parametros.getDescontoSimplificadoIrrf().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal baseSimplificada = bruto.subtract(parametros.getDescontoSimplificadoIrrf());
                irrfSimplificado = calcularIrrfBase(baseSimplificada, parametros);
            }
            return irrfConvencional.min(irrfSimplificado).setScale(2, RoundingMode.HALF_UP);
        }

        return irrfConvencional.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcularIrrfBase(BigDecimal base, ParametrosLegislacao parametros) {
        if (base.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        List<FaixaIrrf> faixas = new ArrayList<>(parametros.getFaixasIrrf());
        faixas.sort((f1, f2) -> f1.getFaixaOrdem().compareTo(f2.getFaixaOrdem()));

        FaixaIrrf faixaEncontrada = null;
        for (FaixaIrrf faixa : faixas) {
            BigDecimal limiteMin = faixa.getLimiteMinimo();
            BigDecimal limiteMax = faixa.getLimiteMaximo();
            if (base.compareTo(limiteMin) >= 0 && (limiteMax == null || base.compareTo(limiteMax) <= 0)) {
                faixaEncontrada = faixa;
                break;
            }
        }

        if (faixaEncontrada == null) {
            faixaEncontrada = faixas.get(faixas.size() - 1);
        }

        BigDecimal aliquota = faixaEncontrada.getAliquota().divide(new BigDecimal("100.0"), 4, RoundingMode.HALF_UP);
        BigDecimal parcela = faixaEncontrada.getParcelaDeduzir();

        BigDecimal irrf = base.multiply(aliquota).subtract(parcela);
        return irrf.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP) : irrf.setScale(2, RoundingMode.HALF_UP);
    }
}
