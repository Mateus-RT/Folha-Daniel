package com.folha.pagamento.domain.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ParametrosLegislacao {
    private Long id;
    private Integer ano;
    private Integer mesInicio;
    private BigDecimal salarioMinimo;
    private BigDecimal tetoInss;
    private BigDecimal valorDependenteIrrf;
    private BigDecimal descontoSimplificadoIrrf;
    private BigDecimal aliquotaFgtsClt;
    private BigDecimal aliquotaFgtsAprendiz;
    private BigDecimal aliquotaInssPatronalAprendiz;
    private List<FaixaInss> faixasInss = new ArrayList<>();
    private List<FaixaIrrf> faixasIrrf = new ArrayList<>();

    public ParametrosLegislacao() {}

    public ParametrosLegislacao(Long id, Integer ano, Integer mesInicio, BigDecimal salarioMinimo, BigDecimal tetoInss,
                                BigDecimal valorDependenteIrrf, BigDecimal descontoSimplificadoIrrf,
                                BigDecimal aliquotaFgtsClt, BigDecimal aliquotaFgtsAprendiz,
                                BigDecimal aliquotaInssPatronalAprendiz, List<FaixaInss> faixasInss, List<FaixaIrrf> faixasIrrf) {
        this.id = id;
        this.ano = ano;
        this.mesInicio = mesInicio;
        this.salarioMinimo = salarioMinimo;
        this.tetoInss = tetoInss;
        this.valorDependenteIrrf = valorDependenteIrrf;
        this.descontoSimplificadoIrrf = descontoSimplificadoIrrf;
        this.aliquotaFgtsClt = aliquotaFgtsClt;
        this.aliquotaFgtsAprendiz = aliquotaFgtsAprendiz;
        this.aliquotaInssPatronalAprendiz = aliquotaInssPatronalAprendiz;
        this.faixasInss = faixasInss;
        this.faixasIrrf = faixasIrrf;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getAno() { return ano; }
    public void setAno(Integer ano) { this.ano = ano; }

    public Integer getMesInicio() { return mesInicio; }
    public void setMesInicio(Integer mesInicio) { this.mesInicio = mesInicio; }

    public BigDecimal getSalarioMinimo() { return salarioMinimo; }
    public void setSalarioMinimo(BigDecimal salarioMinimo) { this.salarioMinimo = salarioMinimo; }

    public BigDecimal getTetoInss() { return tetoInss; }
    public void setTetoInss(BigDecimal tetoInss) { this.tetoInss = tetoInss; }

    public BigDecimal getValorDependenteIrrf() { return valorDependenteIrrf; }
    public void setValorDependenteIrrf(BigDecimal valorDependenteIrrf) { this.valorDependenteIrrf = valorDependenteIrrf; }

    public BigDecimal getDescontoSimplificadoIrrf() { return descontoSimplificadoIrrf; }
    public void setDescontoSimplificadoIrrf(BigDecimal descontoSimplificadoIrrf) { this.descontoSimplificadoIrrf = descontoSimplificadoIrrf; }

    public BigDecimal getAliquotaFgtsClt() { return aliquotaFgtsClt; }
    public void setAliquotaFgtsClt(BigDecimal aliquotaFgtsClt) { this.aliquotaFgtsClt = aliquotaFgtsClt; }

    public BigDecimal getAliquotaFgtsAprendiz() { return aliquotaFgtsAprendiz; }
    public void setAliquotaFgtsAprendiz(BigDecimal aliquotaFgtsAprendiz) { this.aliquotaFgtsAprendiz = aliquotaFgtsAprendiz; }

    public BigDecimal getAliquotaInssPatronalAprendiz() { return aliquotaInssPatronalAprendiz; }
    public void setAliquotaInssPatronalAprendiz(BigDecimal aliquotaInssPatronalAprendiz) { this.aliquotaInssPatronalAprendiz = aliquotaInssPatronalAprendiz; }

    public List<FaixaInss> getFaixasInss() { return faixasInss; }
    public void setFaixasInss(List<FaixaInss> faixasInss) { this.faixasInss = faixasInss; }

    public List<FaixaIrrf> getFaixasIrrf() { return faixasIrrf; }
    public void setFaixasIrrf(List<FaixaIrrf> faixasIrrf) { this.faixasIrrf = faixasIrrf; }
}
