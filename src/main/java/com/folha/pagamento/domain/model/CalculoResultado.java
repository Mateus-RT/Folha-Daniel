package com.folha.pagamento.domain.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CalculoResultado {
    private Long id;
    private Long funcionarioId;
    private int mes;
    private int ano;
    private RegimeTrabalho regimeTrabalho;
    private BigDecimal salarioBase;
    private BigDecimal totalVencimentos;
    private BigDecimal totalDescontos;
    private BigDecimal salarioLiquido;
    private BigDecimal valorFgts;
    private String cargo;
    private List<VerbaCalculada> verbas = new ArrayList<>();

    public CalculoResultado() {}

    public CalculoResultado(Long id, Long funcionarioId, int mes, int ano, RegimeTrabalho regimeTrabalho,
                            BigDecimal salarioBase, BigDecimal totalVencimentos, BigDecimal totalDescontos,
                            BigDecimal salarioLiquido, BigDecimal valorFgts, List<VerbaCalculada> verbas) {
        this.id = id;
        this.funcionarioId = funcionarioId;
        this.mes = mes;
        this.ano = ano;
        this.regimeTrabalho = regimeTrabalho;
        this.salarioBase = salarioBase;
        this.totalVencimentos = totalVencimentos;
        this.totalDescontos = totalDescontos;
        this.salarioLiquido = salarioLiquido;
        this.valorFgts = valorFgts;
        this.verbas = verbas;
    }

    public CalculoResultado(Long id, Long funcionarioId, int mes, int ano, RegimeTrabalho regimeTrabalho,
                            BigDecimal salarioBase, BigDecimal totalVencimentos, BigDecimal totalDescontos,
                            BigDecimal salarioLiquido, BigDecimal valorFgts, String cargo, List<VerbaCalculada> verbas) {
        this.id = id;
        this.funcionarioId = funcionarioId;
        this.mes = mes;
        this.ano = ano;
        this.regimeTrabalho = regimeTrabalho;
        this.salarioBase = salarioBase;
        this.totalVencimentos = totalVencimentos;
        this.totalDescontos = totalDescontos;
        this.salarioLiquido = salarioLiquido;
        this.valorFgts = valorFgts;
        this.cargo = cargo;
        this.verbas = verbas;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getFuncionarioId() { return funcionarioId; }
    public void setFuncionarioId(Long funcionarioId) { this.funcionarioId = funcionarioId; }

    public int getMes() { return mes; }
    public void setMes(int mes) { this.mes = mes; }

    public int getAno() { return ano; }
    public void setAno(int ano) { this.ano = ano; }

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

    public List<VerbaCalculada> getVerbas() { return verbas; }
    public void setVerbas(List<VerbaCalculada> verbas) { this.verbas = verbas; }
}
