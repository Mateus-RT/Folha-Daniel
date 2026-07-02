package com.folha.pagamento.presentation.cli;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MenuRunner implements CommandLineRunner {

    private final FolhaPagamentoCliCommands cliCommands;

    @Value("${app.cli.enabled:true}")
    private boolean cliEnabled;

    public MenuRunner(FolhaPagamentoCliCommands cliCommands) {
        this.cliCommands = cliCommands;
    }

    @Override
    public void run(String... args) throws Exception {
        if (cliEnabled) {
            cliCommands.iniciarMenu();
        }
    }
}
