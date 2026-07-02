package com.folha.pagamento.presentation.cli;

import com.folha.pagamento.application.usecase.CalcularFolhaUseCase;
import com.folha.pagamento.application.usecase.ConsultarHistoricoUseCase;
import com.folha.pagamento.application.usecase.ManterFuncionarioUseCase;
import com.folha.pagamento.domain.model.*;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@ShellComponent
public class FolhaPagamentoCliCommands {

    private final ManterFuncionarioUseCase manterFuncionarioUseCase;
    private final CalcularFolhaUseCase calcularFolhaUseCase;
    private final ConsultarHistoricoUseCase consultarHistoricoUseCase;

    public FolhaPagamentoCliCommands(ManterFuncionarioUseCase manterFuncionarioUseCase,
                                     CalcularFolhaUseCase calcularFolhaUseCase,
                                     ConsultarHistoricoUseCase consultarHistoricoUseCase) {
        this.manterFuncionarioUseCase = manterFuncionarioUseCase;
        this.calcularFolhaUseCase = calcularFolhaUseCase;
        this.consultarHistoricoUseCase = consultarHistoricoUseCase;
    }

    @ShellMethod(key = "wizard", value = "Inicia o console interativo de menus numerados")
    public void iniciarMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=================================");
            System.out.println("SISTEMA DE FOLHA DE PAGAMENTO PF");
            System.out.println("=================================");
            System.out.println("1. Cadastrar Funcionário");
            System.out.println("2. Calcular Folha de Pagamento");
            System.out.println("3. Alterar Funcionário (Salário/Cargo)");
            System.out.println("4. Visualizar Histórico de Cálculos");
            System.out.println("5. Listar todos os funcionários");
            System.out.println("6. Inativar funcionário");
            System.out.println("7. Calcular em lote");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            System.out.flush();

            String opcao = scanner.nextLine().strip();

            try {
                if (opcao.equals("1")) {
                    cadastrarFuncionario(scanner);
                } else if (opcao.equals("2")) {
                    calcularFolha(scanner);
                } else if (opcao.equals("3")) {
                    alterarFuncionario(scanner);
                } else if (opcao.equals("4")) {
                    visualizarHistorico(scanner);
                } else if (opcao.equals("5")) {
                    listarFuncionarios(scanner);
                } else if (opcao.equals("6")) {
                    inativarFuncionario(scanner);
                } else if (opcao.equals("7")) {
                    calcularFolhaEmLote(scanner);
                } else if (opcao.equals("0") || opcao.equalsIgnoreCase("SAIR")) {
                    System.out.println("Encerrando o sistema. Até logo!");
                    System.exit(0);
                    break;
                } else {
                    System.out.println("Opção inválida. Por favor, selecione uma opção entre 0 e 7.");
                }
            } catch (SairException e) {
                // Captura cancelamento e volta ao menu principal
            } catch (Exception e) {
                System.out.println("Erro inesperado: " + e.getMessage());
            }
        }
    }

    private void cadastrarFuncionario(Scanner scanner) {
        System.out.println("\n--- CADASTRO DE FUNCIONÁRIO ---");
        String nome = obterInputString(scanner, "Nome: ");
        
        String cpf = "";
        while (true) {
            cpf = obterInputString(scanner, "CPF (somente números, 11 dígitos): ");
            if (!ManterFuncionarioUseCase.validarCpf(cpf)) {
                System.out.println("Erro: CPF inválido. Por favor, digite um CPF válido ou 'SAIR' para cancelar.");
                continue;
            }
            break;
        }

        List<String> regimesValidos = Arrays.asList("CLT", "PJ", "COMISSIONADO", "ESTAGIO", "JOVEM_APRENDIZ");
        String regimeStr = "";
        while (true) {
            regimeStr = obterInputString(scanner, "Regime (CLT, PJ, COMISSIONADO, ESTAGIO, JOVEM_APRENDIZ): ").toUpperCase();
            if (!regimesValidos.contains(regimeStr)) {
                System.out.println("Erro: Regime inválido. Escolha um dos seguintes regimes: CLT, PJ, COMISSIONADO, ESTAGIO, JOVEM_APRENDIZ.");
                continue;
            }
            break;
        }

        RegimeTrabalho regime = RegimeTrabalho.valueOf(regimeStr);
        Funcionario funcionario = new Funcionario();
        funcionario.setNome(nome);
        funcionario.setCpf(cpf);
        funcionario.setRegimeTrabalho(regime);
        funcionario.setStatus(StatusFuncionario.ATIVO);
        funcionario.setDataAdmissao(LocalDate.now());

        if (regime == RegimeTrabalho.CLT) {
            BigDecimal salario = BigDecimal.ZERO;
            while (true) {
                salario = obterInputBigDecimal(scanner, "Salário Base: ", BigDecimal.valueOf(0.01),
                        "Erro: Salário inválido. Deve ser um número.",
                        "Erro: O valor deve ser maior que zero.");
                if (salario.compareTo(new BigDecimal("1518.00")) < 0) {
                    System.out.println("Erro: O salário base para CLT não pode ser menor que o salário mínimo vigente (R$ 1.518,00).");
                    continue;
                }
                break;
            }
            BigDecimal horaExtra = obterInputBigDecimal(scanner, "Adicional Hora Extra: ", BigDecimal.ZERO,
                    "Erro: Valor inválido.", "Erro: Valor deve ser positivo.");
            BigDecimal adNoturno = obterInputBigDecimal(scanner, "Adicional Noturno: ", BigDecimal.ZERO,
                    "Erro: Valor inválido.", "Erro: Valor deve ser positivo.");
            String cargo = obterInputString(scanner, "Cargo: ");

            funcionario.setSalarioBase(salario);
            funcionario.setHoraExtra(horaExtra);
            funcionario.setAdicionalNoturno(adNoturno);
            funcionario.setCargo(cargo);

        } else if (regime == RegimeTrabalho.PJ) {
            BigDecimal contrato = obterInputBigDecimal(scanner, "Valor do Contrato: ", BigDecimal.valueOf(0.01),
                    "Erro: Valor inválido.", "Erro: O valor deve ser maior que zero.");
            BigDecimal iss = BigDecimal.ZERO;
            while (true) {
                iss = obterInputBigDecimal(scanner, "Alíquota de ISS (%): ", BigDecimal.ZERO,
                        "Erro: Valor inválido.", "Erro: Alíquota de ISS deve estar entre 0% e 100%.");
                if (iss.compareTo(BigDecimal.valueOf(100.0)) > 0) {
                    System.out.println("Erro: Alíquota de ISS deve estar entre 0% e 100%.");
                    continue;
                }
                break;
            }
            String cargo = obterInputString(scanner, "Cargo: ");

            funcionario.setSalarioBase(contrato);
            funcionario.setAliquotaIssPj(iss);
            funcionario.setCargo(cargo);

        } else if (regime == RegimeTrabalho.COMISSIONADO) {
            BigDecimal salarioFixo = obterInputBigDecimal(scanner, "Salário Fixo: ", BigDecimal.valueOf(0.01),
                    "Erro: Valor inválido.", "Erro: O valor deve ser maior que zero.");
            String cargo = obterInputString(scanner, "Cargo: ");

            funcionario.setSalarioBase(salarioFixo);
            funcionario.setCargo(cargo);

        } else if (regime == RegimeTrabalho.ESTAGIO) {
            BigDecimal bolsa = obterInputBigDecimal(scanner, "Valor da Bolsa: ", BigDecimal.valueOf(0.01),
                    "Erro: Valor inválido.", "Erro: O valor deve ser maior que zero.");
            BigDecimal transporte = obterInputBigDecimal(scanner, "Auxílio Transporte: ", BigDecimal.ZERO,
                    "Erro: Valor inválido.", "Erro: Valor deve ser positivo.");
            String cargo = obterInputString(scanner, "Cargo: ");

            funcionario.setSalarioBase(bolsa);
            funcionario.setAuxilioTransporte(transporte);
            funcionario.setCargo(cargo);

        } else if (regime == RegimeTrabalho.JOVEM_APRENDIZ) {
            BigDecimal salarioProp = obterInputBigDecimal(scanner, "Salário Proporcional: ", BigDecimal.valueOf(0.01),
                    "Erro: Valor inválido.", "Erro: O valor deve ser maior que zero.");
            String cargo = obterInputString(scanner, "Cargo: ");

            funcionario.setSalarioBase(salarioProp);
            funcionario.setCargo(cargo);
        }

        try {
            manterFuncionarioUseCase.cadastrar(funcionario);
            System.out.println("Funcionário cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void calcularFolha(Scanner scanner) {
        System.out.println("\n--- CÁLCULO DE FOLHA DE PAGAMENTO ---");
        String cpf = obterInputString(scanner, "CPF do Funcionário: ");
        
        Optional<Funcionario> optFunc = manterFuncionarioUseCase.buscarPorCpf(cpf);
        if (optFunc.isEmpty()) {
            System.out.println("Erro: Funcionário não cadastrado.");
            return;
        }

        Funcionario funcionario = optFunc.get();

        int mes = 0;
        while (true) {
            System.out.print("Mês de referência (1-12): ");
            System.out.flush();
            String mesStr = scanner.nextLine().strip();
            if (mesStr.equalsIgnoreCase("SAIR")) throw new SairException();
            try {
                mes = Integer.parseInt(mesStr);
                if (mes < 1 || mes > 12) {
                    System.out.println("Erro: Mês inválido. Deve ser um número entre 1 e 12.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Erro: Mês inválido. Deve ser um número entre 1 e 12.");
            }
        }

        int ano = 0;
        while (true) {
            System.out.print("Ano de referência (2023-2025): ");
            System.out.flush();
            String anoStr = scanner.nextLine().strip();
            if (anoStr.equalsIgnoreCase("SAIR")) throw new SairException();
            try {
                ano = Integer.parseInt(anoStr);
                if (ano < 2023 || ano > 2025) {
                    System.out.println("Erro: Ano inválido. O sistema suporta apenas os anos de 2023 a 2025.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Erro: Ano inválido. O sistema suporta apenas os anos de 2023 a 2025.");
            }
        }

        // Configura parâmetros dinâmicos no funcionário antes do cálculo
        if (funcionario.getRegimeTrabalho() == RegimeTrabalho.PJ) {
            while (true) {
                System.out.print("Deseja aplicar dedução de ISS? (S/N): ");
                System.out.flush();
                String op = scanner.nextLine().strip().toUpperCase();
                if (op.equalsIgnoreCase("SAIR")) throw new SairException();
                if (!op.equals("S") && !op.equals("N")) {
                    System.out.println("Erro: Digite S para Sim ou N para Não.");
                    continue;
                }
                funcionario.setAplicarIssCalculo(op.equals("S"));
                break;
            }
        } else if (funcionario.getRegimeTrabalho() == RegimeTrabalho.COMISSIONADO) {
            BigDecimal comissao = obterInputBigDecimal(scanner, "Valor das comissões deste mês: ", BigDecimal.ZERO,
                    "Erro: Valor inválido.", "Erro: Valor deve ser positivo.");
            funcionario.setComissaoMensal(comissao);
        }

        try {
            CalculoResultado resultado = calcularFolhaUseCase.calcularIndividual(cpf, mes, ano, funcionario.getComissaoMensal(), funcionario.getAplicarIssCalculo());
            exibirDemonstrativo(resultado, funcionario.getNome(), cpf);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void exibirDemonstrativo(CalculoResultado calc, String nome, String cpf) {
        System.out.println("\n--- DEMONSTRATIVO DE PAGAMENTO ---");
        System.out.println(f("CPF: %s", cpf));
        System.out.println(f("Nome: %s", nome));
        System.out.println(f("Regime: %s", calc.getRegimeTrabalho().name()));
        System.out.println(f("Mês/Ano: %02d/%d", calc.getMes(), calc.getAno()));
        
        // Exibe o cargo salvo no cálculo
        String cargoExibido = calc.getCargo();
        if (cargoExibido == null || cargoExibido.isEmpty()) {
            Optional<Funcionario> fOpt = manterFuncionarioUseCase.buscarPorCpf(cpf);
            cargoExibido = fOpt.map(Funcionario::getCargo).orElse("");
        }

        System.out.println(f("Cargo: %s", cargoExibido));
        System.out.println(f("Salário Base: R$ %.2f", calc.getSalarioBase()));
        System.out.println(f("Salário Bruto: R$ %.2f", calc.getTotalVencimentos()));

        BigDecimal inss = calc.getVerbas().stream()
                .filter(v -> v.getTipo() == TipoVerba.DESCONTO && v.getNome().startsWith("INSS"))
                .map(VerbaCalculada::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal irrf = calc.getVerbas().stream()
                .filter(v -> v.getTipo() == TipoVerba.DESCONTO && v.getNome().equals("IRRF"))
                .map(VerbaCalculada::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal outrosDescontos = calc.getVerbas().stream()
                .filter(v -> v.getTipo() == TipoVerba.DESCONTO && !v.getNome().startsWith("INSS") && !v.getNome().equals("IRRF") && !v.getNome().equals("Dedução ISS"))
                .map(VerbaCalculada::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Se for PJ com dedução ISS, o ISS é exibido como "Outros Descontos/Deduções" no demonstrativo!
        BigDecimal iss = calc.getVerbas().stream()
                .filter(v -> v.getTipo() == TipoVerba.DESCONTO && v.getNome().equals("Dedução ISS"))
                .map(VerbaCalculada::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        outrosDescontos = outrosDescontos.add(iss);

        System.out.println(f("Desconto INSS: R$ %.2f", inss));
        System.out.println(f("Desconto IRRF: R$ %.2f", irrf));
        System.out.println(f("Outros Descontos/Deduções: R$ %.2f", outrosDescontos));
        System.out.println(f("FGTS (Empresa): R$ %.2f", calc.getValorFgts()));
        System.out.println(f("Salário Líquido: R$ %.2f", calc.getSalarioLiquido()));
        System.out.println("----------------------------------");
    }

    private void alterarFuncionario(Scanner scanner) {
        System.out.println("\n--- ALTERAR FUNCIONÁRIO ---");
        String cpf = obterInputString(scanner, "CPF do Funcionário: ");

        Optional<Funcionario> optFunc = manterFuncionarioUseCase.buscarPorCpf(cpf);
        if (optFunc.isEmpty()) {
            System.out.println("Erro: Funcionário não cadastrado.");
            return;
        }

        Funcionario funcionario = optFunc.get();
        RegimeTrabalho regime = funcionario.getRegimeTrabalho();
        System.out.println(f("Funcionário encontrado: %s (Regime: %s, Cargo Atual: %s)", 
                funcionario.getNome(), regime.name(), funcionario.getCargo()));

        BigDecimal novoSalario = BigDecimal.ZERO;
        if (regime == RegimeTrabalho.CLT) {
            while (true) {
                novoSalario = obterInputBigDecimal(scanner, "Novo Salário Base: ", BigDecimal.valueOf(0.01),
                        "Erro: Salário inválido. Deve ser um número.",
                        "Erro: O valor deve ser maior que zero.");
                if (novoSalario.compareTo(new BigDecimal("1518.00")) < 0) {
                    System.out.println("Erro: O salário base para CLT não pode ser menor que o salário mínimo vigente (R$ 1.518,00).");
                    continue;
                }
                break;
            }
        } else if (regime == RegimeTrabalho.PJ) {
            novoSalario = obterInputBigDecimal(scanner, "Novo Valor do Contrato: ", BigDecimal.valueOf(0.01),
                    "Erro: Valor inválido.", "Erro: O valor deve ser maior que zero.");
        } else if (regime == RegimeTrabalho.COMISSIONADO) {
            novoSalario = obterInputBigDecimal(scanner, "Novo Salário Fixo: ", BigDecimal.valueOf(0.01),
                    "Erro: Valor inválido.", "Erro: O valor deve ser maior que zero.");
        } else if (regime == RegimeTrabalho.ESTAGIO) {
            novoSalario = obterInputBigDecimal(scanner, "Novo Valor da Bolsa: ", BigDecimal.valueOf(0.01),
                    "Erro: Valor inválido.", "Erro: O valor deve ser maior que zero.");
        } else if (regime == RegimeTrabalho.JOVEM_APRENDIZ) {
            novoSalario = obterInputBigDecimal(scanner, "Novo Salário Proporcional: ", BigDecimal.valueOf(0.01),
                    "Erro: Valor inválido.", "Erro: O valor deve ser maior que zero.");
        }

        String novoCargo = obterInputString(scanner, "Novo Cargo: ");

        try {
            manterFuncionarioUseCase.alterar(cpf, novoSalario, novoCargo);
            System.out.println("Funcionário alterado com sucesso!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void visualizarHistorico(Scanner scanner) {
        System.out.println("\n--- HISTÓRICO DE CÁLCULOS ---");
        String cpf = obterInputString(scanner, "CPF do Funcionário: ");

        Optional<Funcionario> optFunc = manterFuncionarioUseCase.buscarPorCpf(cpf);
        if (optFunc.isEmpty()) {
            System.out.println("Erro: Funcionário não cadastrado.");
            return;
        }

        List<CalculoResultado> calculos = consultarHistoricoUseCase.buscarPorCpf(cpf);
        if (calculos.isEmpty()) {
            System.out.println("Não há cálculos de folha registrados para este funcionário.");
            return;
        }

        System.out.println(f("Histórico para o CPF %s (Nome: %s):", cpf, optFunc.get().getNome()));
        int idx = 1;
        
        List<CalculoResultado> ordenados = new ArrayList<>(calculos);
        ordenados.sort(Comparator.comparingInt(CalculoResultado::getAno).thenComparingInt(CalculoResultado::getMes));

        for (CalculoResultado calc : ordenados) {
            System.out.println(f("\n[%d] Cálculo referente a %02d/%d", idx++, calc.getMes(), calc.getAno()));
            
            String cargoEpoca = calc.getCargo();
            if (cargoEpoca == null || cargoEpoca.isEmpty()) {
                cargoEpoca = optFunc.get().getCargo();
            }

            System.out.println(f("  Cargo à época: %s", cargoEpoca));
            System.out.println(f("  Regime: %s", calc.getRegimeTrabalho().name()));
            System.out.println(f("  Salário Base/Referência: R$ %.2f", calc.getSalarioBase()));
            System.out.println(f("  Salário Bruto: R$ %.2f", calc.getTotalVencimentos()));

            BigDecimal inss = calc.getVerbas().stream()
                    .filter(v -> v.getTipo() == TipoVerba.DESCONTO && v.getNome().startsWith("INSS"))
                    .map(VerbaCalculada::getValor)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal irrf = calc.getVerbas().stream()
                    .filter(v -> v.getTipo() == TipoVerba.DESCONTO && v.getNome().equals("IRRF"))
                    .map(VerbaCalculada::getValor)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal outrosDescontos = calc.getVerbas().stream()
                    .filter(v -> v.getTipo() == TipoVerba.DESCONTO && !v.getNome().startsWith("INSS") && !v.getNome().equals("IRRF") && !v.getNome().equals("Dedução ISS"))
                    .map(VerbaCalculada::getValor)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal iss = calc.getVerbas().stream()
                    .filter(v -> v.getTipo() == TipoVerba.DESCONTO && v.getNome().equals("Dedução ISS"))
                    .map(VerbaCalculada::getValor)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            outrosDescontos = outrosDescontos.add(iss);

            System.out.println(f("  Desconto INSS: R$ %.2f", inss));
            System.out.println(f("  Desconto IRRF: R$ %.2f", irrf));
            System.out.println(f("  Outros Descontos/Deduções: R$ %.2f", outrosDescontos));
            System.out.println(f("  FGTS (Empresa): R$ %.2f", calc.getValorFgts()));
            System.out.println(f("  Salário Líquido: R$ %.2f", calc.getSalarioLiquido()));
        }
        System.out.println("----------------------------------");
    }

    private String obterInputString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            System.out.flush();
            String input = scanner.nextLine().strip();
            if (input.equalsIgnoreCase("SAIR")) {
                throw new SairException();
            }
            if (input.isEmpty()) {
                System.out.println("Erro: Campo obrigatório.");
                continue;
            }
            return input;
        }
    }

    private BigDecimal obterInputBigDecimal(Scanner scanner, String prompt, BigDecimal minPermitido,
                                             String msgErroInvalido, String msgErroLimite) {
        while (true) {
            System.out.print(prompt);
            System.out.flush();
            String input = scanner.nextLine().strip();
            if (input.equalsIgnoreCase("SAIR")) {
                throw new SairException();
            }
            try {
                BigDecimal valor = new BigDecimal(input);
                if (valor.compareTo(minPermitido) < 0) {
                    System.out.println(msgErroLimite);
                    continue;
                }
                return valor;
            } catch (NumberFormatException e) {
                System.out.println(msgErroInvalido);
            }
        }
    }

    private String f(String format, Object... args) {
        return String.format(Locale.US, format, args);
    }

    private void listarFuncionarios(Scanner scanner) {
        System.out.println("\n--- LISTAGEM DE FUNCIONÁRIOS ---");
        List<Funcionario> funcionarios = manterFuncionarioUseCase.buscarTodos();
        if (funcionarios.isEmpty()) {
            System.out.println("Nenhum funcionário cadastrado.");
            return;
        }

        System.out.println(f("%-20s | %-11s | %-15s | %-8s | %-12s", "Nome", "CPF", "Regime", "Status", "Salário Base"));
        System.out.println("-----------------------------------------------------------------------------");
        for (Funcionario f : funcionarios) {
            BigDecimal salario = f.getSalarioBase() != null ? f.getSalarioBase() : BigDecimal.ZERO;
            System.out.println(f("%-20s | %-11s | %-15s | %-8s | R$ %10.2f",
                    f.getNome().length() > 20 ? f.getNome().substring(0, 17) + "..." : f.getNome(),
                    f.getCpf(),
                    f.getRegimeTrabalho().name(),
                    f.getStatus().name(),
                    salario));
        }
        System.out.println("-----------------------------------------------------------------------------");
    }

    private void inativarFuncionario(Scanner scanner) {
        System.out.println("\n--- INATIVAR FUNCIONÁRIO ---");
        String cpf = obterInputString(scanner, "CPF do Funcionário: ");

        Optional<Funcionario> optFunc = manterFuncionarioUseCase.buscarPorCpf(cpf);
        if (optFunc.isEmpty()) {
            System.out.println("Erro: Funcionário não cadastrado.");
            return;
        }

        Funcionario f = optFunc.get();
        System.out.println(f("Funcionário encontrado: %s", f.getNome()));

        System.out.print("Confirma a inativação deste funcionário? (S/N): ");
        System.out.flush();
        String confirmacao = scanner.nextLine().strip().toUpperCase();
        if (confirmacao.equalsIgnoreCase("SAIR")) throw new SairException();

        if (confirmacao.equals("S")) {
            try {
                manterFuncionarioUseCase.inativar(cpf);
                System.out.println("Funcionário inativado com sucesso!");
            } catch (Exception e) {
                System.out.println("Erro ao inativar funcionário: " + e.getMessage());
            }
        } else {
            System.out.println("Inativação cancelada.");
        }
    }

    private void calcularFolhaEmLote(Scanner scanner) {
        System.out.println("\n--- CÁLCULO DE FOLHA EM LOTE ---");

        int mes = 0;
        while (true) {
            System.out.print("Mês de referência (1-12): ");
            System.out.flush();
            String mesStr = scanner.nextLine().strip();
            if (mesStr.equalsIgnoreCase("SAIR")) throw new SairException();
            try {
                mes = Integer.parseInt(mesStr);
                if (mes < 1 || mes > 12) {
                    System.out.println("Erro: Mês inválido. Deve ser um número entre 1 e 12.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Erro: Mês inválido. Deve ser um número entre 1 e 12.");
            }
        }

        int ano = 0;
        while (true) {
            System.out.print("Ano de referência (2023-2025): ");
            System.out.flush();
            String anoStr = scanner.nextLine().strip();
            if (anoStr.equalsIgnoreCase("SAIR")) throw new SairException();
            try {
                ano = Integer.parseInt(anoStr);
                if (ano < 2023 || ano > 2025) {
                    System.out.println("Erro: Ano inválido. O sistema suporta apenas os anos de 2023 a 2025.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Erro: Ano inválido. O sistema suporta apenas os anos de 2023 a 2025.");
            }
        }

        try {
            List<CalculoResultado> resultados = calcularFolhaUseCase.calcularEmLote(mes, ano);
            System.out.println(f("Cálculo em lote concluído com sucesso. Total de folhas processadas: %d", resultados.size()));
        } catch (Exception e) {
            System.out.println("Erro ao calcular folha em lote: " + e.getMessage());
        }
    }
}
