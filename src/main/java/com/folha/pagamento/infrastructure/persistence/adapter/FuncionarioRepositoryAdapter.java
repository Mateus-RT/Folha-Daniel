package com.folha.pagamento.infrastructure.persistence.adapter;

import com.folha.pagamento.domain.model.Funcionario;
import com.folha.pagamento.domain.model.StatusFuncionario;
import com.folha.pagamento.domain.repository.FuncionarioRepository;
import com.folha.pagamento.infrastructure.persistence.entity.FuncionarioJpaEntity;
import com.folha.pagamento.infrastructure.persistence.mapper.FuncionarioMapper;
import com.folha.pagamento.infrastructure.persistence.repository.FuncionarioJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class FuncionarioRepositoryAdapter implements FuncionarioRepository {

    private final FuncionarioJpaRepository jpaRepository;

    public FuncionarioRepositoryAdapter(FuncionarioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Funcionario salvar(Funcionario funcionario) {
        FuncionarioJpaEntity entity = FuncionarioMapper.toJpaEntity(funcionario);
        FuncionarioJpaEntity saved = jpaRepository.save(entity);
        return FuncionarioMapper.toDomain(saved);
    }

    @Override
    public Optional<Funcionario> buscarPorCpf(String cpf) {
        return jpaRepository.findByCpf(cpf).map(FuncionarioMapper::toDomain);
    }

    @Override
    public Optional<Funcionario> buscarPorId(Long id) {
        return jpaRepository.findById(id).map(FuncionarioMapper::toDomain);
    }

    @Override
    public List<Funcionario> buscarTodos() {
        return jpaRepository.findAll().stream()
                .map(FuncionarioMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Funcionario> buscarTodosAtivos() {
        return jpaRepository.findByStatus(StatusFuncionario.ATIVO).stream()
                .map(FuncionarioMapper::toDomain)
                .collect(Collectors.toList());
    }
}
