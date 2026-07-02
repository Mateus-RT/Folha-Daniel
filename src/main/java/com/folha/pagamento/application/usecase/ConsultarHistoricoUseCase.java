package com.folha.pagamento.application.usecase;

import com.folha.pagamento.domain.model.CalculoResultado;
import com.folha.pagamento.domain.repository.HistoricoCalculoFolhaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultarHistoricoUseCase {

    private final HistoricoCalculoFolhaRepository repository;

    public ConsultarHistoricoUseCase(HistoricoCalculoFolhaRepository repository) {
        this.repository = repository;
    }

    public List<CalculoResultado> buscarPorCpf(String cpf) {
        return repository.buscarPorFuncionarioCpf(cpf);
    }

    public List<CalculoResultado> buscarPorFuncionarioId(Long funcionarioId) {
        return repository.buscarPorFuncionarioId(funcionarioId);
    }
}
