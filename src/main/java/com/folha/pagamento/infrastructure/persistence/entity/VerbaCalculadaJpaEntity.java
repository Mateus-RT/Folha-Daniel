package com.folha.pagamento.infrastructure.persistence.entity;

import com.folha.pagamento.domain.model.TipoVerba;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "verba_calculada")
public class VerbaCalculadaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "historico_calculo_id", nullable = false)
    private HistoricoCalculoFolhaJpaEntity historicoCalculo;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 15)
    private TipoVerba tipo;

    @Column(name = "valor", nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Column(name = "referencia", length = 50)
    private String referencia;

    public VerbaCalculadaJpaEntity() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public HistoricoCalculoFolhaJpaEntity getHistoricoCalculo() { return historicoCalculo; }
    public void setHistoricoCalculo(HistoricoCalculoFolhaJpaEntity historicoCalculo) { this.historicoCalculo = historicoCalculo; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public TipoVerba getTipo() { return tipo; }
    public void setTipo(TipoVerba tipo) { this.tipo = tipo; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }
}
