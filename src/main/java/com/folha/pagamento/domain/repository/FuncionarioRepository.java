package com.folha.pagamento.domain.repository;

import com.folha.pagamento.domain.model.Funcionario;
import java.util.List;
import java.util.Optional;

public interface FuncionarioRepository {
    Funcionario salvar(Funcionario funcionario);
    Optional<Funcionario> buscarPorCpf(String cpf);
    Optional<Funcionario> buscarPorId(Long id);
    List<Funcionario> buscarTodos();
    List<Funcionario> buscarTodosAtivos();
}
