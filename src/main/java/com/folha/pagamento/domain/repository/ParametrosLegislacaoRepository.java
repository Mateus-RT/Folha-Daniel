package com.folha.pagamento.domain.repository;

import com.folha.pagamento.domain.model.ParametrosLegislacao;
import java.util.Optional;

public interface ParametrosLegislacaoRepository {
    ParametrosLegislacao buscarPorAno(int ano);
    Optional<ParametrosLegislacao> buscarPorAnoEMes(int ano, int mes);
}
