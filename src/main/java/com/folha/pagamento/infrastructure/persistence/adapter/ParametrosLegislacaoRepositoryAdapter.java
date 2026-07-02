package com.folha.pagamento.infrastructure.persistence.adapter;

import com.folha.pagamento.domain.model.ParametrosLegislacao;
import com.folha.pagamento.domain.repository.ParametrosLegislacaoRepository;
import com.folha.pagamento.infrastructure.persistence.entity.ParametrosLegislacaoJpaEntity;
import com.folha.pagamento.infrastructure.persistence.mapper.ParametrosLegislacaoMapper;
import com.folha.pagamento.infrastructure.persistence.repository.ParametrosLegislacaoJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ParametrosLegislacaoRepositoryAdapter implements ParametrosLegislacaoRepository {

    private final ParametrosLegislacaoJpaRepository jpaRepository;

    public ParametrosLegislacaoRepositoryAdapter(ParametrosLegislacaoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ParametrosLegislacao buscarPorAno(int ano) {
        return jpaRepository.findFirstByAnoOrderByMesInicioDesc(ano)
                .map(ParametrosLegislacaoMapper::toDomain)
                .orElse(null);
    }

    @Override
    public Optional<ParametrosLegislacao> buscarPorAnoEMes(int ano, int mes) {
        // Busca a vigência aplicável no mês ou a primeira do ano
        Optional<ParametrosLegislacaoJpaEntity> opt = jpaRepository.findFirstByAnoAndMesInicioLessThanEqualOrderByMesInicioDesc(ano, mes);
        if (opt.isPresent()) {
            return opt.map(ParametrosLegislacaoMapper::toDomain);
        }
        // Fallback: se não achar nenhuma menor ou igual ao mês solicitado, pega a primeira cadastrada para aquele ano
        return jpaRepository.findFirstByAnoOrderByMesInicioDesc(ano).map(ParametrosLegislacaoMapper::toDomain);
    }
}
