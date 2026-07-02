package com.folha.pagamento.infrastructure.persistence.repository;

import com.folha.pagamento.infrastructure.persistence.entity.FuncionarioJpaEntity;
import com.folha.pagamento.domain.model.StatusFuncionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FuncionarioJpaRepository extends JpaRepository<FuncionarioJpaEntity, Long> {
    Optional<FuncionarioJpaEntity> findByCpf(String cpf);
    List<FuncionarioJpaEntity> findByStatus(StatusFuncionario status);
}
