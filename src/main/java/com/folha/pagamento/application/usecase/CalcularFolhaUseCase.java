package com.folha.pagamento.application.usecase;

import com.folha.pagamento.domain.model.CalculoResultado;
import com.folha.pagamento.domain.model.Funcionario;
import com.folha.pagamento.domain.model.ParametrosLegislacao;
import com.folha.pagamento.domain.repository.FuncionarioRepository;
import com.folha.pagamento.domain.repository.HistoricoCalculoFolhaRepository;
import com.folha.pagamento.domain.repository.ParametrosLegislacaoRepository;
import com.folha.pagamento.domain.service.CalculadoraFolha;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalcularFolhaUseCase {

    private final FuncionarioRepository funcionarioRepository;
    private final ParametrosLegislacaoRepository legislacaoRepository;
    private final HistoricoCalculoFolhaRepository historicoRepository;
    private final CalculadoraFolha calculadoraFolha;

    public CalcularFolhaUseCase(FuncionarioRepository funcionarioRepository,
                                 ParametrosLegislacaoRepository legislacaoRepository,
                                 HistoricoCalculoFolhaRepository historicoRepository,
                                 CalculadoraFolha calculadoraFolha) {
        this.funcionarioRepository = funcionarioRepository;
        this.legislacaoRepository = legislacaoRepository;
        this.historicoRepository = historicoRepository;
        this.calculadoraFolha = calculadoraFolha;
    }

    /**
     * Calcula a folha individual passando dados transientes do CLI (comissão, ISS).
     * Os dados transientes não são persistidos — servem apenas para este cálculo.
     */
    @Transactional
    public CalculoResultado calcularIndividual(String cpf, int mes, int ano,
                                               BigDecimal comissaoMensal,
                                               Boolean aplicarIssCalculo) {
        Funcionario funcionario = funcionarioRepository.buscarPorCpf(cpf)
                .orElseThrow(() -> new IllegalArgumentException("Erro: Funcionário não cadastrado."));

        // Injeta dados transientes do CLI sem alterar o banco
        if (comissaoMensal != null) {
            funcionario.setComissaoMensal(comissaoMensal);
        }
        if (aplicarIssCalculo != null) {
            funcionario.setAplicarIssCalculo(aplicarIssCalculo);
        }

        ParametrosLegislacao parametros = legislacaoRepository.buscarPorAnoEMes(ano, mes)
                .orElseThrow(() -> new IllegalArgumentException("Erro: Legislação não encontrada para o período " + mes + "/" + ano));

        CalculoResultado resultado = calculadoraFolha.calcular(funcionario, mes, ano, parametros);
        return historicoRepository.salvar(resultado);
    }

    /**
     * Sobrecarga simples sem dados transientes (uso em testes e lote).
     */
    @Transactional
    public CalculoResultado calcularIndividual(String cpf, int mes, int ano) {
        return calcularIndividual(cpf, mes, ano, null, null);
    }

    @Transactional
    public List<CalculoResultado> calcularEmLote(int mes, int ano) {
        List<Funcionario> ativos = funcionarioRepository.buscarTodosAtivos();
        ParametrosLegislacao parametros = legislacaoRepository.buscarPorAnoEMes(ano, mes)
                .orElseThrow(() -> new IllegalArgumentException("Erro: Legislação não encontrada para o período " + mes + "/" + ano));

        List<CalculoResultado> resultados = new ArrayList<>();
        for (Funcionario funcionario : ativos) {
            CalculoResultado resultado = calculadoraFolha.calcular(funcionario, mes, ano, parametros);
            CalculoResultado salvo = historicoRepository.salvar(resultado);
            resultados.add(salvo);
        }
        return resultados;
    }
}
