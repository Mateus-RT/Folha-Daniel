package com.folha.pagamento.domain.model;

import java.math.BigDecimal;

public class VerbaCalculada {
    private Long id;
    private String nome;
    private TipoVerba tipo;
    private BigDecimal valor;
    private String referencia;

    public VerbaCalculada() {}

    public VerbaCalculada(String nome, TipoVerba tipo, BigDecimal valor, String referencia) {
        this.nome = nome;
        this.tipo = tipo;
        this.valor = valor;
        this.referencia = referencia;
    }

    public VerbaCalculada(Long id, String nome, TipoVerba tipo, BigDecimal valor, String referencia) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.valor = valor;
        this.referencia = referencia;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public TipoVerba getTipo() { return tipo; }
    public void setTipo(TipoVerba tipo) { this.tipo = tipo; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }
}
