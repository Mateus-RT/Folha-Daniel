package com.folha.pagamento.infrastructure.persistence.repository;

import com.folha.pagamento.infrastructure.persistence.entity.ParametrosLegislacaoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParametrosLegislacaoJpaRepository extends JpaRepository<ParametrosLegislacaoJpaEntity, Long> {
    List<ParametrosLegislacaoJpaEntity> findByAno(Integer ano);
    Optional<ParametrosLegislacaoJpaEntity> findFirstByAnoAndMesInicioLessThanEqualOrderByMesInicioDesc(Integer ano, Integer mes);
    Optional<ParametrosLegislacaoJpaEntity> findFirstByAnoOrderByMesInicioDesc(Integer ano);
}
