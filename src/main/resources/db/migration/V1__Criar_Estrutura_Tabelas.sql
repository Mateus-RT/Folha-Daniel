-- 1. Tabela de Funcionários
CREATE TABLE funcionario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    regime_trabalho VARCHAR(30) NOT NULL,
    status VARCHAR(20) NOT NULL,
    data_admissao DATE NOT NULL,
    data_rescisao DATE,
    salario_base NUMERIC(15, 2) NOT NULL,
    percentual_comissao NUMERIC(5, 2), -- Nulo se não for comissionado
    jornada_mensal_horas INTEGER NOT NULL DEFAULT 220,
    deducao_iss_pj BOOLEAN NOT NULL DEFAULT FALSE, -- Flag para PJ
    aliquota_iss_pj NUMERIC(5, 2),
    quantidade_dependentes INTEGER NOT NULL DEFAULT 0,
    tipo_inss_rpa VARCHAR(20), -- 'ONZE_PORCENTO' ou 'VINTE_PORCENTO' para RPA
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT chk_regime_trabalho CHECK (regime_trabalho IN ('CLT', 'PJ', 'RPA', 'COMISSIONADO', 'ESTAGIO', 'JOVEM_APRENDIZ')),
    CONSTRAINT chk_status CHECK (status IN ('ATIVO', 'INATIVO', 'AFASTADO')),
    CONSTRAINT chk_tipo_inss_rpa CHECK (tipo_inss_rpa IN ('ONZE_PORCENTO', 'VINTE_PORCENTO'))
);

-- Índices para otimização de buscas por CPF (login/consulta)
CREATE INDEX idx_funcionario_cpf ON funcionario(cpf);

-- 2. Tabela de Parâmetros Anuais da Legislação
CREATE TABLE parametros_legislacao (
    id BIGSERIAL PRIMARY KEY,
    ano INTEGER NOT NULL,
    mes_inicio INTEGER NOT NULL,
    salario_minimo NUMERIC(15, 2) NOT NULL,
    teto_inss NUMERIC(15, 2) NOT NULL,
    valor_dependente_irrf NUMERIC(15, 2) NOT NULL,
    desconto_simplificado_irrf NUMERIC(15, 2) NOT NULL,
    aliquota_fgts_clt NUMERIC(5, 2) NOT NULL DEFAULT 8.00,
    aliquota_fgts_aprendiz NUMERIC(5, 2) NOT NULL DEFAULT 2.00,
    aliquota_inss_patronal_aprendiz NUMERIC(5, 2) NOT NULL DEFAULT 5.00,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_ano_mes UNIQUE (ano, mes_inicio),
    CONSTRAINT chk_mes_inicio CHECK (mes_inicio BETWEEN 1 AND 12)
);

-- 3. Tabela de Faixas Progressivas do INSS (1:N com parametros_legislacao)
CREATE TABLE faixa_inss (
    id BIGSERIAL PRIMARY KEY,
    parametros_legislacao_id BIGINT NOT NULL,
    faixa_ordem INTEGER NOT NULL,
    limite_minimo NUMERIC(15, 2) NOT NULL,
    limite_maximo NUMERIC(15, 2), -- Nulo para a última faixa (acima do teto)
    aliquota NUMERIC(5, 2) NOT NULL,
    
    FOREIGN KEY (parametros_legislacao_id) REFERENCES parametros_legislacao(id) ON DELETE CASCADE,
    CONSTRAINT uq_faixa_inss_parametro_ordem UNIQUE (parametros_legislacao_id, faixa_ordem)
);

-- 4. Tabela de Faixas Progressivas do IRRF (1:N com parametros_legislacao)
CREATE TABLE faixa_irrf (
    id BIGSERIAL PRIMARY KEY,
    parametros_legislacao_id BIGINT NOT NULL,
    faixa_ordem INTEGER NOT NULL,
    limite_minimo NUMERIC(15, 2) NOT NULL,
    limite_maximo NUMERIC(15, 2), -- Nulo para a última faixa
    aliquota NUMERIC(5, 2) NOT NULL,
    parcela_deduzir NUMERIC(15, 2) NOT NULL,
    
    FOREIGN KEY (parametros_legislacao_id) REFERENCES parametros_legislacao(id) ON DELETE CASCADE,
    CONSTRAINT uq_faixa_irrf_parametro_ordem UNIQUE (parametros_legislacao_id, faixa_ordem)
);

-- 5. Tabela de Histórico de Cálculo da Folha (Mestre)
CREATE TABLE historico_calculo_folha (
    id BIGSERIAL PRIMARY KEY,
    funcionario_id BIGINT NOT NULL,
    mes INTEGER NOT NULL,
    ano INTEGER NOT NULL,
    data_calculo TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    regime_trabalho VARCHAR(30) NOT NULL, -- Copiado no momento do cálculo
    salario_base NUMERIC(15, 2) NOT NULL,
    total_vencimentos NUMERIC(15, 2) NOT NULL,
    total_descontos NUMERIC(15, 2) NOT NULL,
    salario_liquido NUMERIC(15, 2) NOT NULL,
    valor_fgts NUMERIC(15, 2) NOT NULL DEFAULT 0.00, -- Informativo/Encargo patronal
    
    FOREIGN KEY (funcionario_id) REFERENCES funcionario(id) ON DELETE RESTRICT,
    CONSTRAINT uq_funcionario_periodo UNIQUE (funcionario_id, mes, ano),
    CONSTRAINT chk_mes CHECK (mes BETWEEN 1 AND 12),
    CONSTRAINT chk_ano CHECK (ano >= 2000)
);

-- Índice para busca rápida de histórico de funcionários por período
CREATE INDEX idx_historico_periodo ON historico_calculo_folha(mes, ano);
CREATE INDEX idx_historico_funcionario ON historico_calculo_folha(funcionario_id);

-- 6. Tabela de Detalhes das Verbas Calculadas (Detalhe)
CREATE TABLE verba_calculada (
    id BIGSERIAL PRIMARY KEY,
    historico_calculo_id BIGINT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    tipo VARCHAR(15) NOT NULL, -- 'PROVENTO', 'DESCONTO', 'INFORMATIVA'
    valor NUMERIC(15, 2) NOT NULL,
    referencia VARCHAR(50), -- Detalhes textuais adicionais (Ex: "Alíquota 9%", "5 dependentes")
    
    FOREIGN KEY (historico_calculo_id) REFERENCES historico_calculo_folha(id) ON DELETE CASCADE,
    CONSTRAINT chk_tipo_verba CHECK (tipo IN ('PROVENTO', 'DESCONTO', 'INFORMATIVA'))
);

CREATE INDEX idx_verba_historico ON verba_calculada(historico_calculo_id);
