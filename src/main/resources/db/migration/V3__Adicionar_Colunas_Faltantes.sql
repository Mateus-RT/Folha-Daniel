-- Adicionar colunas faltantes para suportar a lógica de negócios CLT, Estágio e Histórico
ALTER TABLE funcionario ADD COLUMN cargo VARCHAR(100);
ALTER TABLE funcionario ADD COLUMN hora_extra NUMERIC(15, 2) DEFAULT 0.00;
ALTER TABLE funcionario ADD COLUMN adicional_noturno NUMERIC(15, 2) DEFAULT 0.00;
ALTER TABLE funcionario ADD COLUMN auxilio_transporte NUMERIC(15, 2) DEFAULT 0.00;

ALTER TABLE historico_calculo_folha ADD COLUMN cargo VARCHAR(100);
