package com.folha.pagamento.domain.model;

import java.math.BigDecimal;

public class FaixaInss {
    private Long id;
    private Integer faixaOrdem;
    private BigDecimal limiteMinimo;
    private BigDecimal limiteMaximo;
    private BigDecimal aliquota;

    public FaixaInss() {}

    public FaixaInss(Long id, Integer faixaOrdem, BigDecimal limiteMinimo, BigDecimal limiteMaximo, BigDecimal aliquota) {
        this.id = id;
        this.faixaOrdem = faixaOrdem;
        this.limiteMinimo = limiteMinimo;
        this.limiteMaximo = limiteMaximo;
        this.aliquota = aliquota;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getFaixaOrdem() { return faixaOrdem; }
    public void setFaixaOrdem(Integer faixaOrdem) { this.faixaOrdem = faixaOrdem; }

    public BigDecimal getLimiteMinimo() { return limiteMinimo; }
    public void setLimiteMinimo(BigDecimal limiteMinimo) { this.limiteMinimo = limiteMinimo; }

    public BigDecimal getLimiteMaximo() { return limiteMaximo; }
    public void setLimiteMaximo(BigDecimal limiteMaximo) { this.limiteMaximo = limiteMaximo; }

    public BigDecimal getAliquota() { return aliquota; }
    public void setAliquota(BigDecimal aliquota) { this.aliquota = aliquota; }
}
