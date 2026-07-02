-- Inserção de Parâmetros de Legislação (Vigências 2023, 2024 e 2025)
INSERT INTO parametros_legislacao (ano, mes_inicio, salario_minimo, teto_inss, valor_dependente_irrf, desconto_simplificado_irrf, aliquota_fgts_clt, aliquota_fgts_aprendiz, aliquota_inss_patronal_aprendiz)
VALUES
(2023, 1, 1302.00, 7507.49, 189.59, 0.00, 8.00, 2.00, 5.00),
(2023, 5, 1320.00, 7507.49, 189.59, 528.00, 8.00, 2.00, 5.00),
(2024, 1, 1412.00, 7786.02, 189.59, 564.80, 8.00, 2.00, 5.00),
(2025, 1, 1518.00, 8157.41, 189.59, 564.80, 8.00, 2.00, 5.00);

-- Faixas INSS - 2023 (Vigência 1 - Jan a Apr)
INSERT INTO faixa_inss (parametros_legislacao_id, faixa_ordem, limite_minimo, limite_maximo, aliquota) VALUES
((SELECT id FROM parametros_legislacao WHERE ano = 2023 AND mes_inicio = 1), 1, 0.00, 1302.00, 7.50),
((SELECT id FROM parametros_legislacao WHERE ano = 2023 AND mes_inicio = 1), 2, 1302.01, 2571.29, 9.00),
((SELECT id FROM parametros_legislacao WHERE ano = 2023 AND mes_inicio = 1), 3, 2571.30, 3856.94, 12.00),
((SELECT id FROM parametros_legislacao WHERE ano = 2023 AND mes_inicio = 1), 4, 3856.95, 7507.49, 14.00);

-- Faixas INSS - 2023 (Vigência 2 - Mai a Dez)
INSERT INTO faixa_inss (parametros_legislacao_id, faixa_ordem, limite_minimo, limite_maximo, aliquota) VALUES
((SELECT id FROM parametros_legislacao WHERE ano = 2023 AND mes_inicio = 5), 1, 0.00, 1320.00, 7.50),
((SELECT id FROM parametros_legislacao WHERE ano = 2023 AND mes_inicio = 5), 2, 1320.01, 2571.29, 9.00),
((SELECT id FROM parametros_legislacao WHERE ano = 2023 AND mes_inicio = 5), 3, 2571.30, 3856.94, 12.00),
((SELECT id FROM parametros_legislacao WHERE ano = 2023 AND mes_inicio = 5), 4, 3856.95, 7507.49, 14.00);

-- Faixas INSS - 2024 (Vigência 1)
INSERT INTO faixa_inss (parametros_legislacao_id, faixa_ordem, limite_minimo, limite_maximo, aliquota) VALUES
((SELECT id FROM parametros_legislacao WHERE ano = 2024 AND mes_inicio = 1), 1, 0.00, 1412.00, 7.50),
((SELECT id FROM parametros_legislacao WHERE ano = 2024 AND mes_inicio = 1), 2, 1412.01, 2666.68, 9.00),
((SELECT id FROM parametros_legislacao WHERE ano = 2024 AND mes_inicio = 1), 3, 2666.69, 4000.03, 12.00),
((SELECT id FROM parametros_legislacao WHERE ano = 2024 AND mes_inicio = 1), 4, 4000.04, 7786.02, 14.00);

-- Faixas INSS - 2025 (Vigência 1)
INSERT INTO faixa_inss (parametros_legislacao_id, faixa_ordem, limite_minimo, limite_maximo, aliquota) VALUES
((SELECT id FROM parametros_legislacao WHERE ano = 2025 AND mes_inicio = 1), 1, 0.00, 1518.00, 7.50),
((SELECT id FROM parametros_legislacao WHERE ano = 2025 AND mes_inicio = 1), 2, 1518.01, 2793.88, 9.00),
((SELECT id FROM parametros_legislacao WHERE ano = 2025 AND mes_inicio = 1), 3, 2793.89, 4190.83, 12.00),
((SELECT id FROM parametros_legislacao WHERE ano = 2025 AND mes_inicio = 1), 4, 4190.84, 8157.41, 14.00);

-- Faixas IRRF - 2023 (Vigência 1 - Jan a Apr)
INSERT INTO faixa_irrf (parametros_legislacao_id, faixa_ordem, limite_minimo, limite_maximo, aliquota, parcela_deduzir) VALUES
((SELECT id FROM parametros_legislacao WHERE ano = 2023 AND mes_inicio = 1), 1, 0.00, 1903.98, 0.00, 0.00),
((SELECT id FROM parametros_legislacao WHERE ano = 2023 AND mes_inicio = 1), 2, 1903.99, 2826.65, 7.50, 142.80),
((SELECT id FROM parametros_legislacao WHERE ano = 2023 AND mes_inicio = 1), 3, 2826.66, 3751.05, 15.00, 354.80),
((SELECT id FROM parametros_legislacao WHERE ano = 2023 AND mes_inicio = 1), 4, 3751.06, 4664.68, 22.50, 636.13),
((SELECT id FROM parametros_legislacao WHERE ano = 2023 AND mes_inicio = 1), 5, 4664.69, NULL, 27.50, 869.36);

-- Faixas IRRF - 2023 (Vigência 2 - Mai a Dez)
INSERT INTO faixa_irrf (parametros_legislacao_id, faixa_ordem, limite_minimo, limite_maximo, aliquota, parcela_deduzir) VALUES
((SELECT id FROM parametros_legislacao WHERE ano = 2023 AND mes_inicio = 5), 1, 0.00, 2112.00, 0.00, 0.00),
((SELECT id FROM parametros_legislacao WHERE ano = 2023 AND mes_inicio = 5), 2, 2112.01, 2826.65, 7.50, 158.40),
((SELECT id FROM parametros_legislacao WHERE ano = 2023 AND mes_inicio = 5), 3, 2826.66, 3751.05, 15.00, 370.40),
((SELECT id FROM parametros_legislacao WHERE ano = 2023 AND mes_inicio = 5), 4, 3751.06, 4664.68, 22.50, 651.73),
((SELECT id FROM parametros_legislacao WHERE ano = 2023 AND mes_inicio = 5), 5, 4664.69, NULL, 27.50, 884.96);

-- Faixas IRRF - 2024 (Vigência 1)
INSERT INTO faixa_irrf (parametros_legislacao_id, faixa_ordem, limite_minimo, limite_maximo, aliquota, parcela_deduzir) VALUES
((SELECT id FROM parametros_legislacao WHERE ano = 2024 AND mes_inicio = 1), 1, 0.00, 2259.20, 0.00, 0.00),
((SELECT id FROM parametros_legislacao WHERE ano = 2024 AND mes_inicio = 1), 2, 2259.21, 2826.65, 7.50, 169.44),
((SELECT id FROM parametros_legislacao WHERE ano = 2024 AND mes_inicio = 1), 3, 2826.66, 3751.05, 15.00, 381.44),
((SELECT id FROM parametros_legislacao WHERE ano = 2024 AND mes_inicio = 1), 4, 3751.06, 4664.68, 22.50, 662.77),
((SELECT id FROM parametros_legislacao WHERE ano = 2024 AND mes_inicio = 1), 5, 4664.69, NULL, 27.50, 896.00);

-- Faixas IRRF - 2025 (Vigência 1)
INSERT INTO faixa_irrf (parametros_legislacao_id, faixa_ordem, limite_minimo, limite_maximo, aliquota, parcela_deduzir) VALUES
((SELECT id FROM parametros_legislacao WHERE ano = 2025 AND mes_inicio = 1), 1, 0.00, 2259.20, 0.00, 0.00),
((SELECT id FROM parametros_legislacao WHERE ano = 2025 AND mes_inicio = 1), 2, 2259.21, 2826.65, 7.50, 169.44),
((SELECT id FROM parametros_legislacao WHERE ano = 2025 AND mes_inicio = 1), 3, 2826.66, 3751.05, 15.00, 381.44),
((SELECT id FROM parametros_legislacao WHERE ano = 2025 AND mes_inicio = 1), 4, 3751.06, 4664.68, 22.50, 662.77),
((SELECT id FROM parametros_legislacao WHERE ano = 2025 AND mes_inicio = 1), 5, 4664.69, NULL, 27.50, 896.00);
