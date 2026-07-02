package com.folha.pagamento.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Funcionario {
    private Long id;
    private String nome;
    private String cpf;
    private RegimeTrabalho regimeTrabalho;
    private StatusFuncionario status;
    private LocalDate dataAdmissao;
    private LocalDate dataRescisao;
    private BigDecimal salarioBase;
    private BigDecimal percentualComissao;
    private Integer jornadaMensalHoras;
    private Boolean deducaoIssPj;
    private BigDecimal aliquotaIssPj;
    private Integer quantidadeDependentes;
    private String tipoInssRpa;
    private String cargo;
    private BigDecimal horaExtra;
    private BigDecimal adicionalNoturno;
    private BigDecimal auxilioTransporte;

    // Campos transientes para cálculo de folha
    private BigDecimal comissaoMensal;
    private Boolean aplicarIssCalculo;

    public Funcionario() {}

    public Funcionario(Long id, String nome, String cpf, RegimeTrabalho regimeTrabalho, StatusFuncionario status,
                       LocalDate dataAdmissao, LocalDate dataRescisao, BigDecimal salarioBase,
                       BigDecimal percentualComissao, Integer jornadaMensalHoras, Boolean deducaoIssPj,
                       BigDecimal aliquotaIssPj, Integer quantidadeDependentes, String tipoInssRpa,
                       String cargo, BigDecimal horaExtra, BigDecimal adicionalNoturno, BigDecimal auxilioTransporte) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.regimeTrabalho = regimeTrabalho;
        this.status = status;
        this.dataAdmissao = dataAdmissao;
        this.dataRescisao = dataRescisao;
        this.salarioBase = salarioBase;
        this.percentualComissao = percentualComissao;
        this.jornadaMensalHoras = jornadaMensalHoras;
        this.deducaoIssPj = deducaoIssPj;
        this.aliquotaIssPj = aliquotaIssPj;
        this.quantidadeDependentes = quantidadeDependentes;
        this.tipoInssRpa = tipoInssRpa;
        this.cargo = cargo;
        this.horaExtra = horaExtra;
        this.adicionalNoturno = adicionalNoturno;
        this.auxilioTransporte = auxilioTransporte;
    }

    // Getters e Setters
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

    public BigDecimal getComissaoMensal() { return comissaoMensal; }
    public void setComissaoMensal(BigDecimal comissaoMensal) { this.comissaoMensal = comissaoMensal; }

    public Boolean getAplicarIssCalculo() { return aplicarIssCalculo; }
    public void setAplicarIssCalculo(Boolean aplicarIssCalculo) { this.aplicarIssCalculo = aplicarIssCalculo; }
}
