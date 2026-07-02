package com.folha.pagamento.infrastructure.persistence.repository;

import com.folha.pagamento.infrastructure.persistence.entity.HistoricoCalculoFolhaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistoricoCalculoFolhaJpaRepository extends JpaRepository<HistoricoCalculoFolhaJpaEntity, Long> {
    List<HistoricoCalculoFolhaJpaEntity> findByFuncionarioIdOrderByAnoDescMesDesc(Long funcionarioId);
    List<HistoricoCalculoFolhaJpaEntity> findByFuncionarioCpfOrderByAnoDescMesDesc(String cpf);
    Optional<HistoricoCalculoFolhaJpaEntity> findByFuncionarioIdAndMesAndAno(Long funcionarioId, int mes, int ano);
}
