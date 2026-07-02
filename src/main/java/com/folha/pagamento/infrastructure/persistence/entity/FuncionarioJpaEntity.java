package com.folha.pagamento.infrastructure.persistence.entity;

import com.folha.pagamento.domain.model.RegimeTrabalho;
import com.folha.pagamento.domain.model.StatusFuncionario;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "funcionario")
public class FuncionarioJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @Column(name = "cpf", nullable = false, unique = true, length = 11)
    private String cpf;

    @Enumerated(EnumType.STRING)
    @Column(name = "regime_trabalho", nullable = false, length = 30)
    private RegimeTrabalho regimeTrabalho;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusFuncionario status;

    @Column(name = "data_admissao", nullable = false)
    private LocalDate dataAdmissao;

    @Column(name = "data_rescisao")
    private LocalDate dataRescisao;

    @Column(name = "salario_base", nullable = false, precision = 15, scale = 2)
    private BigDecimal salarioBase;

    @Column(name = "percentual_comissao", precision = 5, scale = 2)
    private BigDecimal percentualComissao;

    @Column(name = "jornada_mensal_horas", nullable = false)
    private Integer jornadaMensalHoras = 220;

    @Column(name = "deducao_iss_pj", nullable = false)
    private Boolean deducaoIssPj = false;

    @Column(name = "aliquota_iss_pj", precision = 5, scale = 2)
    private BigDecimal aliquotaIssPj;

    @Column(name = "quantidade_dependentes", nullable = false)
    private Integer quantidadeDependentes = 0;

    @Column(name = "tipo_inss_rpa", length = 20)
    private String tipoInssRpa;

    @Column(name = "cargo", length = 100)
    private String cargo;

    @Column(name = "hora_extra", precision = 15, scale = 2)
    private BigDecimal horaExtra = BigDecimal.ZERO;

    @Column(name = "adicional_noturno", precision = 15, scale = 2)
    private BigDecimal adicionalNoturno = BigDecimal.ZERO;

    @Column(name = "auxilio_transporte", precision = 15, scale = 2)
    private BigDecimal auxilioTransporte = BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public FuncionarioJpaEntity() {}

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (jornadaMensalHoras == null) jornadaMensalHoras = 220;
        if (deducaoIssPj == null) deducaoIssPj = false;
        if (quantidadeDependentes == null) quantidadeDependentes = 0;
        if (horaExtra == null) horaExtra = BigDecimal.ZERO;
        if (adicionalNoturno == null) adicionalNoturno = BigDecimal.ZERO;
        if (auxilioTransporte == null) auxilioTransporte = BigDecimal.ZERO;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public RegimeTrabalho getRegimeTrabalho() { return regimeTrabalho; }
    public void setRegimeTrabalho(RegimeTrabalho regimeTrabalho) { this.regimeTrabalho = regimeTrabalho; }

    public StatusFuncionario getStatus() { return status; }
    public void setStatus(StatusFuncionario status) { this.status = status; }

    public LocalDate getDataAdmissao() { return dataAdmissao; }
    public void setDataAdmissao(LocalDate dataAdmissao) { this.dataAdmissao = dataAdmissao; }

    public LocalDate getDataRescisao() { return dataRescisao; }
    public void setDataRescisao(LocalDate dataRescisao) { this.dataRescisao = dataRescisao; }

    public BigDecimal getSalarioBase() { return salarioBase; }
    public void setSalarioBase(BigDecimal salarioBase) { this.salarioBase = salarioBase; }

    public BigDecimal getPercentualComissao() { return percentualComissao; }
    public void setPercentualComissao(BigDecimal percentualComissao) { this.percentualComissao = percentualComissao; }

    public Integer getJornadaMensalHoras() { return jornadaMensalHoras; }
    public void setJornadaMensalHoras(Integer jornadaMensalHoras) { this.jornadaMensalHoras = jornadaMensalHoras; }

    public Boolean getDeducaoIssPj() { return deducaoIssPj; }
    public void setDeducaoIssPj(Boolean deducaoIssPj) { this.deducaoIssPj = deducaoIssPj; }

    public BigDecimal getAliquotaIssPj() { return aliquotaIssPj; }
    public void setAliquotaIssPj(BigDecimal aliquotaIssPj) { this.aliquotaIssPj = aliquotaIssPj; }

    public Integer getQuantidadeDependentes() { return quantidadeDependentes; }
    public void setQuantidadeDependentes(Integer quantidadeDependentes) { this.quantidadeDependentes = quantidadeDependentes; }

    public String getTipoInssRpa() { return tipoInssRpa; }
    public void setTipoInssRpa(String tipoInssRpa) { this.tipoInssRpa = tipoInssRpa; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public BigDecimal getHoraExtra() { return horaExtra; }
    public void setHoraExtra(BigDecimal horaExtra) { this.horaExtra = horaExtra; }

    public BigDecimal getAdicionalNoturno() { return adicionalNoturno; }
    public void setAdicionalNoturno(BigDecimal adicionalNoturno) { this.adicionalNoturno = adicionalNoturno; }

    public BigDecimal getAuxilioTransporte() { return auxilioTransporte; }
    public void setAuxilioTransporte(BigDecimal auxilioTransporte) { this.auxilioTransporte = auxilioTransporte; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
