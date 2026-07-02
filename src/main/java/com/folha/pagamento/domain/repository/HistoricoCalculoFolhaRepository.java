package com.folha.pagamento.domain.repository;

import com.folha.pagamento.domain.model.CalculoResultado;
import java.util.List;
import java.util.Optional;

public interface HistoricoCalculoFolhaRepository {
    CalculoResultado salvar(CalculoResultado resultado);
    List<CalculoResultado> buscarPorFuncionarioId(Long funcionarioId);
    List<CalculoResultado> buscarPorFuncionarioCpf(String cpf);
    Optional<CalculoResultado> buscarPorFuncionarioEPeriodo(Long funcionarioId, int mes, int ano);
}
