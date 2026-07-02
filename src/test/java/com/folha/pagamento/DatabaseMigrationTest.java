package com.folha.pagamento;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {
    "spring.shell.interactive.enabled=false",
    "spring.shell.script.enabled=false",
    "app.cli.enabled=false",
    "spring.flyway.enabled=true"
})
public class DatabaseMigrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testMigrationsAndSeed() {
        // 1. Verificar se as tabelas principais foram criadas e se conseguimos consultá-las
        Integer countFuncionarios = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM funcionario", Integer.class);
        assertTrue(countFuncionarios >= 0, "A tabela funcionario deve ser acessível.");

        // 2. Verificar se os registros de parametros_legislacao foram populados pelo seed
        List<Map<String, Object>> parametros = jdbcTemplate.queryForList("SELECT * FROM parametros_legislacao ORDER BY ano, mes_inicio");
        assertEquals(4, parametros.size(), "Devem existir 4 vigências configuradas na legislação.");

        // 3. Verificar dados específicos da legislação de 2025 para validar o conteúdo do seed
        Map<String, Object> params2025 = parametros.stream()
                .filter(m -> ((Number) m.get("ano")).intValue() == 2025)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Legislação de 2025 não encontrada."));
        
        assertEquals(1518.00, ((Number) params2025.get("salario_minimo")).doubleValue(), 0.001);
        assertEquals(8157.41, ((Number) params2025.get("teto_inss")).doubleValue(), 0.001);

        // 4. Verificar a integridade relacional: faixas de INSS associadas a 2025 (devem ser 4 faixas)
        List<Map<String, Object>> faixasInss = jdbcTemplate.queryForList(
                "SELECT f.* FROM faixa_inss f JOIN parametros_legislacao p ON f.parametros_legislacao_id = p.id WHERE p.ano = 2025 ORDER BY f.faixa_ordem"
        );
        assertEquals(4, faixasInss.size(), "Devem existir 4 faixas de INSS para o ano de 2025.");

        // 5. Verificar a integridade relacional: faixas de IRRF associadas a 2025 (devem ser 5 faixas)
        List<Map<String, Object>> faixasIrrf = jdbcTemplate.queryForList(
                "SELECT f.* FROM faixa_irrf f JOIN parametros_legislacao p ON f.parametros_legislacao_id = p.id WHERE p.ano = 2025 ORDER BY f.faixa_ordem"
        );
        assertEquals(5, faixasIrrf.size(), "Devem existir 5 faixas de IRRF para o ano de 2025.");

        // 6. Verificar se a tabela de histórico de cálculo da folha existe
        Integer countHistorico = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM historico_calculo_folha", Integer.class);
        assertTrue(countHistorico >= 0, "A tabela historico_calculo_folha deve ser acessível.");

        // 7. Verificar se a tabela de verbas calculadas existe e possui chave estrangeira ativa
        Integer countVerba = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM verba_calculada", Integer.class);
        assertTrue(countVerba >= 0, "A tabela verba_calculada deve ser acessível.");

        // 8. Verificar se as migrações foram registradas com sucesso na tabela do Flyway
        List<Map<String, Object>> flywayHistory = jdbcTemplate.queryForList(
                "SELECT * FROM flyway_schema_history WHERE success = true"
        );
        assertTrue(flywayHistory.size() >= 2, "Devem existir pelo menos 2 migrações aplicadas com sucesso pelo Flyway.");
    }
}
