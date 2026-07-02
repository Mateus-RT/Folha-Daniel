package com.folha.pagamento.infrastructure.persistence.mapper;

import com.folha.pagamento.domain.model.CalculoResultado;
import com.folha.pagamento.domain.model.VerbaCalculada;
import com.folha.pagamento.infrastructure.persistence.entity.FuncionarioJpaEntity;
import com.folha.pagamento.infrastructure.persistence.entity.HistoricoCalculoFolhaJpaEntity;
import com.folha.pagamento.infrastructure.persistence.entity.VerbaCalculadaJpaEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HistoricoCalculoMapper {

    public static CalculoResultado toDomain(HistoricoCalculoFolhaJpaEntity jpa) {
        if (jpa == null) return null;

        List<VerbaCalculada> verbas = jpa.getVerbasCalculadas() != null
                ? jpa.getVerbasCalculadas().stream().map(HistoricoCalculoMapper::toDomain).collect(Collectors.toList())
                : new ArrayList<>();

        return new CalculoResultado(
                jpa.getId(),
                jpa.getFuncionario().getId(),
                jpa.getMes(),
                jpa.getAno(),
                jpa.getRegimeTrabalho(),
                jpa.getSalarioBase(),
                jpa.getTotalVencimentos(),
                jpa.getTotalDescontos(),
                jpa.getSalarioLiquido(),
                jpa.getValorFgts(),
                jpa.getCargo(),
                verbas
        );
    }

    public static VerbaCalculada toDomain(VerbaCalculadaJpaEntity jpa) {
        if (jpa == null) return null;
        return new VerbaCalculada(
                jpa.getId(),
                jpa.getNome(),
                jpa.getTipo(),
                jpa.getValor(),
                jpa.getReferencia()
        );
    }

    public static HistoricoCalculoFolhaJpaEntity toJpaEntity(CalculoResultado domain, FuncionarioJpaEntity funcionarioJpa) {
        if (domain == null) return null;
        HistoricoCalculoFolhaJpaEntity jpa = new HistoricoCalculoFolhaJpaEntity();
        jpa.setId(domain.getId());
        jpa.setFuncionario(funcionarioJpa);
        jpa.setMes(domain.getMes());
        jpa.setAno(domain.getAno());
        jpa.setRegimeTrabalho(domain.getRegimeTrabalho());
        jpa.setSalarioBase(domain.getSalarioBase());
        jpa.setTotalVencimentos(domain.getTotalVencimentos());
        jpa.setTotalDescontos(domain.getTotalDescontos());
        jpa.setSalarioLiquido(domain.getSalarioLiquido());
        jpa.setValorFgts(domain.getValorFgts());
        
        if (domain.getCargo() != null) {
            jpa.setCargo(domain.getCargo());
        } else if (funcionarioJpa != null) {
            jpa.setCargo(funcionarioJpa.getCargo());
        }

        if (domain.getVerbas() != null) {
            List<VerbaCalculadaJpaEntity> verbasJpa = domain.getVerbas().stream()
                    .map(v -> toJpaEntity(v, jpa))
                    .collect(Collectors.toList());
            jpa.setVerbasCalculadas(verbasJpa);
        }

        return jpa;
    }

    public static VerbaCalculadaJpaEntity toJpaEntity(VerbaCalculada domain, HistoricoCalculoFolhaJpaEntity historicoJpa) {
        if (domain == null) return null;
        VerbaCalculadaJpaEntity jpa = new VerbaCalculadaJpaEntity();
        jpa.setId(domain.getId());
        jpa.setHistoricoCalculo(historicoJpa);
        jpa.setNome(domain.getNome());
        jpa.setTipo(domain.getTipo());
        jpa.setValor(domain.getValor());
        jpa.setReferencia(domain.getReferencia());
        return jpa;
    }
}
