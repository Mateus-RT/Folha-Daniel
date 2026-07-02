package com.folha.pagamento.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "faixa_inss")
public class FaixaInssJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parametros_legislacao_id", nullable = false)
    private ParametrosLegislacaoJpaEntity parametrosLegislacao;

    @Column(name = "faixa_ordem", nullable = false)
    private Integer faixaOrdem;

    @Column(name = "limite_minimo", nullable = false, precision = 15, scale = 2)
    private BigDecimal limiteMinimo;

    @Column(name = "limite_maximo", precision = 15, scale = 2)
    private BigDecimal limiteMaximo;

    @Column(name = "aliquota", nullable = false, precision = 5, scale = 2)
    private BigDecimal aliquota;

    // Construtor padrão
    public FaixaInssJpaEntity() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ParametrosLegislacaoJpaEntity getParametrosLegislacao() { return parametrosLegislacao; }
    public void setParametrosLegislacao(ParametrosLegislacaoJpaEntity parametrosLegislacao) { this.parametrosLegislacao = parametrosLegislacao; }

    public Integer getFaixaOrdem() { return faixaOrdem; }
    public void setFaixaOrdem(Integer faixaOrdem) { this.faixaOrdem = faixaOrdem; }

    public BigDecimal getLimiteMinimo() { return limiteMinimo; }
    public void setLimiteMinimo(BigDecimal limiteMinimo) { this.limiteMinimo = limiteMinimo; }

    public BigDecimal getLimiteMaximo() { return limiteMaximo; }
    public void setLimiteMaximo(BigDecimal limiteMaximo) { this.limiteMaximo = limiteMaximo; }

    public BigDecimal getAliquota() { return aliquota; }
    public void setAliquota(BigDecimal aliquota) { this.aliquota = aliquota; }
}
