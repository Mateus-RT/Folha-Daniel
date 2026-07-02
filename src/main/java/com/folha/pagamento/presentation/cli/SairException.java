package com.folha.pagamento.presentation.cli;

public class SairException extends RuntimeException {
    public SairException() {
        super("Retornando ao menu principal");
    }
}
