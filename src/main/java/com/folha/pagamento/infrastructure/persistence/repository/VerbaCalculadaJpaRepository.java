package com.folha.pagamento.infrastructure.persistence.repository;

import com.folha.pagamento.infrastructure.persistence.entity.VerbaCalculadaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerbaCalculadaJpaRepository extends JpaRepository<VerbaCalculadaJpaEntity, Long> {
}
