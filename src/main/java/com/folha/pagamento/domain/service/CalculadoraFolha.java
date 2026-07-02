package com.folha.pagamento.domain.service;

import com.folha.pagamento.domain.model.CalculoResultado;
import com.folha.pagamento.domain.model.Funcionario;
import com.folha.pagamento.domain.model.ParametrosLegislacao;

public interface CalculadoraFolha {
    CalculoResultado calcular(Funcionario funcionario, int mes, int ano, ParametrosLegislacao parametros);
}
