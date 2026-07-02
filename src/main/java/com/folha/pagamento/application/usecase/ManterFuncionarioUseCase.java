package com.folha.pagamento.application.usecase;

import com.folha.pagamento.domain.model.Funcionario;
import com.folha.pagamento.domain.model.RegimeTrabalho;
import com.folha.pagamento.domain.model.StatusFuncionario;
import com.folha.pagamento.domain.repository.FuncionarioRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ManterFuncionarioUseCase {

    private final FuncionarioRepository repository;

    public ManterFuncionarioUseCase(FuncionarioRepository repository) {
        this.repository = repository;
    }

    public Funcionario cadastrar(Funcionario funcionario) {
        // Valida CPF
        if (!validarCpf(funcionario.getCpf())) {
            throw new IllegalArgumentException("Erro: CPF inválido.");
        }

        // Valida se já existe
        if (repository.buscarPorCpf(funcionario.getCpf()).isPresent()) {
            throw new IllegalArgumentException("Erro: Funcionário com este CPF já cadastrado.");
        }

        // Valida salário base maior que zero para todos os regimes
        if (funcionario.getSalarioBase() == null || funcionario.getSalarioBase().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Erro: O salário base deve ser maior que zero.");
        }

        // Valida salário CLT
        if (funcionario.getRegimeTrabalho() == RegimeTrabalho.CLT) {
            if (funcionario.getSalarioBase().compareTo(new BigDecimal("1518.00")) < 0) {
                throw new IllegalArgumentException("Erro: O salário base para CLT não pode ser menor que o salário mínimo vigente (R$ 1.518,00).");
            }
        }

        // Preenche campos padrões
        funcionario.setStatus(StatusFuncionario.ATIVO);
        if (funcionario.getDataAdmissao() == null) {
            funcionario.setDataAdmissao(LocalDate.now());
        }

        return repository.salvar(funcionario);
    }

    public Funcionario alterar(String cpf, BigDecimal novoSalario, String novoCargo) {
        if (novoSalario == null || novoSalario.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Erro: O salário base deve ser maior que zero.");
        }

        Funcionario funcionario = repository.buscarPorCpf(cpf)
                .orElseThrow(() -> new IllegalArgumentException("Erro: Funcionário não cadastrado."));

        // Valida salário CLT
        if (funcionario.getRegimeTrabalho() == RegimeTrabalho.CLT) {
            if (novoSalario == null || novoSalario.compareTo(new BigDecimal("1518.00")) < 0) {
                throw new IllegalArgumentException("Erro: O salário base para CLT não pode ser menor que o salário mínimo vigente (R$ 1.518,00).");
            }
        }

        funcionario.setSalarioBase(novoSalario);
        funcionario.setCargo(novoCargo);

        return repository.salvar(funcionario);
    }

    public Optional<Funcionario> buscarPorCpf(String cpf) {
        return repository.buscarPorCpf(cpf);
    }

    public List<Funcionario> buscarTodos() {
        return repository.buscarTodos();
    }

    public Funcionario inativar(String cpf) {
        Funcionario funcionario = repository.buscarPorCpf(cpf)
                .orElseThrow(() -> new IllegalArgumentException("Erro: Funcionário não cadastrado."));
        if (funcionario.getStatus() == StatusFuncionario.INATIVO) {
            throw new IllegalArgumentException("Erro: Funcionário já está inativo.");
        }
        funcionario.setStatus(StatusFuncionario.INATIVO);
        funcionario.setDataRescisao(java.time.LocalDate.now());
        return repository.salvar(funcionario);
    }

    public static boolean validarCpf(String cpf) {
        if (cpf == null) return false;
        cpf = cpf.replaceAll("\\D", "");
        if (cpf.length() != 11) return false;
        if (cpf.matches("(\\d)\\1{10}")) return false;

        // Primeiro dígito
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        int resto = (soma * 10) % 11;
        if (resto == 10 || resto == 11) resto = 0;
        if (resto != Character.getNumericValue(cpf.charAt(9))) return false;

        // Segundo dígito
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        resto = (soma * 10) % 11;
        if (resto == 10 || resto == 11) resto = 0;
        return resto == Character.getNumericValue(cpf.charAt(10));
    }
}
