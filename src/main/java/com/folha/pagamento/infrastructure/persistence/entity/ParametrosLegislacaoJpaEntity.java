package com.folha.pagamento.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parametros_legislacao")
public class ParametrosLegislacaoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ano", nullable = false)
    private Integer ano;

    @Column(name = "mes_inicio", nullable = false)
    private Integer mesInicio;

    @Column(name = "salario_minimo", nullable = false, precision = 15, scale = 2)
    private BigDecimal salarioMinimo;

    @Column(name = "teto_inss", nullable = false, precision = 15, scale = 2)
    private BigDecimal tetoInss;

    @Column(name = "valor_dependente_irrf", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorDependenteIrrf;

    @Column(name = "desconto_simplificado_irrf", nullable = false, precision = 15, scale = 2)
    private BigDecimal descontoSimplificadoIrrf;

    @Column(name = "aliquota_fgts_clt", nullable = false, precision = 5, scale = 2)
    private BigDecimal aliquotaFgtsClt = new BigDecimal("8.00");

    @Column(name = "aliquota_fgts_aprendiz", nullable = false, precision = 5, scale = 2)
    private BigDecimal aliquotaFgtsAprendiz = new BigDecimal("2.00");

    @Column(name = "aliquota_inss_patronal_aprendiz", nullable = false, precision = 5, scale = 2)
    private BigDecimal aliquotaInssPatronalAprendiz = new BigDecimal("5.00");

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "parametrosLegislacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FaixaInssJpaEntity> faixasInss = new ArrayList<>();

    @OneToMany(mappedBy = "parametrosLegislacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FaixaIrrfJpaEntity> faixasIrrf = new ArrayList<>();

    // Construtor padrão
    public ParametrosLegislacaoJpaEntity() {}

    // Getters and Setters
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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<FaixaInssJpaEntity> getFaixasInss() { return faixasInss; }
    public void setFaixasInss(List<FaixaInssJpaEntity> faixasInss) { this.faixasInss = faixasInss; }

    public List<FaixaIrrfJpaEntity> getFaixasIrrf() { return faixasIrrf; }
    public void setFaixasIrrf(List<FaixaIrrfJpaEntity> faixasIrrf) { this.faixasIrrf = faixasIrrf; }
}
