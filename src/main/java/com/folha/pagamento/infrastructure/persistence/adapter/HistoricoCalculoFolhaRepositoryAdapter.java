package com.folha.pagamento.infrastructure.persistence.adapter;

import com.folha.pagamento.domain.model.CalculoResultado;
import com.folha.pagamento.domain.repository.HistoricoCalculoFolhaRepository;
import com.folha.pagamento.infrastructure.persistence.entity.FuncionarioJpaEntity;
import com.folha.pagamento.infrastructure.persistence.entity.HistoricoCalculoFolhaJpaEntity;
import com.folha.pagamento.infrastructure.persistence.mapper.HistoricoCalculoMapper;
import com.folha.pagamento.infrastructure.persistence.repository.FuncionarioJpaRepository;
import com.folha.pagamento.infrastructure.persistence.repository.HistoricoCalculoFolhaJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class HistoricoCalculoFolhaRepositoryAdapter implements HistoricoCalculoFolhaRepository {

    private final HistoricoCalculoFolhaJpaRepository jpaRepository;
    private final FuncionarioJpaRepository funcionarioJpaRepository;

    public HistoricoCalculoFolhaRepositoryAdapter(HistoricoCalculoFolhaJpaRepository jpaRepository,
                                                  FuncionarioJpaRepository funcionarioJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.funcionarioJpaRepository = funcionarioJpaRepository;
    }

    @Override
    @Transactional
    public CalculoResultado salvar(CalculoResultado resultado) {
        FuncionarioJpaEntity func = funcionarioJpaRepository.findById(resultado.getFuncionarioId())
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado: ID " + resultado.getFuncionarioId()));

        // Remove cálculo anterior se já existir para o mesmo funcionário e período para respeitar a UNIQUE constraint
        jpaRepository.findByFuncionarioIdAndMesAndAno(resultado.getFuncionarioId(), resultado.getMes(), resultado.getAno())
                .ifPresent(jpaRepository::delete);

        // Força a atualização do banco de dados antes da inserção
        jpaRepository.flush();

        HistoricoCalculoFolhaJpaEntity entity = HistoricoCalculoMapper.toJpaEntity(resultado, func);
        HistoricoCalculoFolhaJpaEntity saved = jpaRepository.save(entity);
        return HistoricoCalculoMapper.toDomain(saved);
    }

    @Override
    public List<CalculoResultado> buscarPorFuncionarioId(Long funcionarioId) {
        return jpaRepository.findByFuncionarioIdOrderByAnoDescMesDesc(funcionarioId).stream()
                .map(HistoricoCalculoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<CalculoResultado> buscarPorFuncionarioCpf(String cpf) {
        return jpaRepository.findByFuncionarioCpfOrderByAnoDescMesDesc(cpf).stream()
                .map(HistoricoCalculoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CalculoResultado> buscarPorFuncionarioEPeriodo(Long funcionarioId, int mes, int ano) {
        return jpaRepository.findByFuncionarioIdAndMesAndAno(funcionarioId, mes, ano)
                .map(HistoricoCalculoMapper::toDomain);
    }
}
