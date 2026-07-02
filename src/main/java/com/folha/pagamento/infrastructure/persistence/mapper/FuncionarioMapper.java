package com.folha.pagamento.infrastructure.persistence.mapper;

import com.folha.pagamento.domain.model.Funcionario;
import com.folha.pagamento.infrastructure.persistence.entity.FuncionarioJpaEntity;

public class FuncionarioMapper {

    public static Funcionario toDomain(FuncionarioJpaEntity jpa) {
        if (jpa == null) return null;
        return new Funcionario(
                jpa.getId(),
                jpa.getNome(),
                jpa.getCpf(),
                jpa.getRegimeTrabalho(),
                jpa.getStatus(),
                jpa.getDataAdmissao(),
                jpa.getDataRescisao(),
                jpa.getSalarioBase(),
                jpa.getPercentualComissao(),
                jpa.getJornadaMensalHoras(),
                jpa.getDeducaoIssPj(),
                jpa.getAliquotaIssPj(),
                jpa.getQuantidadeDependentes(),
                jpa.getTipoInssRpa(),
                jpa.getCargo(),
                jpa.getHoraExtra(),
                jpa.getAdicionalNoturno(),
                jpa.getAuxilioTransporte()
        );
    }

    public static FuncionarioJpaEntity toJpaEntity(Funcionario domain) {
        if (domain == null) return null;
        FuncionarioJpaEntity jpa = new FuncionarioJpaEntity();
        jpa.setId(domain.getId());
        jpa.setNome(domain.getNome());
        jpa.setCpf(domain.getCpf());
        jpa.setRegimeTrabalho(domain.getRegimeTrabalho());
        jpa.setStatus(domain.getStatus());
        jpa.setDataAdmissao(domain.getDataAdmissao());
        jpa.setDataRescisao(domain.getDataRescisao());
        jpa.setSalarioBase(domain.getSalarioBase());
        jpa.setPercentualComissao(domain.getPercentualComissao());
        jpa.setJornadaMensalHoras(domain.getJornadaMensalHoras());
        jpa.setDeducaoIssPj(domain.getDeducaoIssPj());
        jpa.setAliquotaIssPj(domain.getAliquotaIssPj());
        jpa.setQuantidadeDependentes(domain.getQuantidadeDependentes());
        jpa.setTipoInssRpa(domain.getTipoInssRpa());
        jpa.setCargo(domain.getCargo());
        jpa.setHoraExtra(domain.getHoraExtra());
        jpa.setAdicionalNoturno(domain.getAdicionalNoturno());
        jpa.setAuxilioTransporte(domain.getAuxilioTransporte());
        return jpa;
    }
}
