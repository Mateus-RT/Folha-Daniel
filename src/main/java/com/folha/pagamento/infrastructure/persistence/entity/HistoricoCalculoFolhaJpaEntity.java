package com.folha.pagamento.infrastructure.persistence.entity;

import com.folha.pagamento.domain.model.RegimeTrabalho;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "historico_calculo_folha")
public class HistoricoCalculoFolhaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_id", nullable = false)
    private FuncionarioJpaEntity funcionario;

    @Column(name = "mes", nullable = false)
    private Integer mes;

    @Column(name = "ano", nullable = false)
    private Integer ano;

    @Column(name = "data_calculo", nullable = false)
    private LocalDateTime dataCalculo = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "regime_trabalho", nullable = false, length = 30)
    private RegimeTrabalho regimeTrabalho;

    @Column(name = "salario_base", nullable = false, precision = 15, scale = 2)
    private BigDecimal salarioBase;

    @Column(name = "total_vencimentos", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalVencimentos;

    @Column(name = "total_descontos", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalDescontos;

    @Column(name = "salario_liquido", nullable = false, precision = 15, scale = 2)
    private BigDecimal salarioLiquido;

    @Column(name = "valor_fgts", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorFgts = BigDecimal.ZERO;

    @Column(name = "cargo", length = 100)
    private String cargo;

    @OneToMany(mappedBy = "historicoCalculo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VerbaCalculadaJpaEntity> verbasCalculadas = new ArrayList<>();

    public HistoricoCalculoFolhaJpaEntity() {}

    @PrePersist
    protected void onCreate() {
        dataCalculo = LocalDateTime.now();
        if (valorFgts == null) valorFgts = BigDecimal.ZERO;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public FuncionarioJpaEntity getFuncionario() { return funcionario; }
    public void setFuncionario(FuncionarioJpaEntity funcionario) { this.funcionario = funcionario; }

    public Integer getMes() { return mes; }
    public void setMes(Integer mes) { this.mes = mes; }

    public Integer getAno() { return ano; }
    public void setAno(Integer ano) { this.ano = ano; }

    public LocalDateTime getDataCalculo() { return dataCalculo; }
    public void setDataCalculo(LocalDateTime dataCalculo) { this.dataCalculo = dataCalculo; }

    public RegimeTrabalho getRegimeTrabalho() { return regimeTrabalho; }
    public void setRegimeTrabalho(RegimeTrabalho regimeTrabalho) { this.regimeTrabalho = regimeTrabalho; }

    public BigDecimal getSalarioBase() { return salarioBase; }
    public void setSalarioBase(BigDecimal salarioBase) { this.salarioBase = salarioBase; }

    public BigDecimal getTotalVencimentos() { return totalVencimentos; }
    public void setTotalVencimentos(BigDecimal totalVencimentos) { this.totalVencimentos = totalVencimentos; }

    public BigDecimal getTotalDescontos() { return totalDescontos; }
    public void setTotalDescontos(BigDecimal totalDescontos) { this.totalDescontos = totalDescontos; }

    public BigDecimal getSalarioLiquido() { return salarioLiquido; }
    public void setSalarioLiquido(BigDecimal salarioLiquido) { this.salarioLiquido = salarioLiquido; }

    public BigDecimal getValorFgts() { return valorFgts; }
    public void setValorFgts(BigDecimal valorFgts) { this.valorFgts = valorFgts; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public List<VerbaCalculadaJpaEntity> getVerbasCalculadas() { return verbasCalculadas; }
    public void setVerbasCalculadas(List<VerbaCalculadaJpaEntity> verbasCalculadas) { this.verbasCalculadas = verbasCalculadas; }
}
