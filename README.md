# Folha de Pagamento PF

Este projeto é um sistema de cálculo de folha de pagamento de pessoas físicas e jurídicas prestadoras de serviços. É desenvolvido em Java 21 utilizando Spring Boot, Spring Shell e persistência relacional com PostgreSQL e controle de migrações via Flyway.

A arquitetura do projeto segue os princípios de **Clean Architecture** e **SOLID**, permitindo uma separação clara entre as regras de negócio puras (domínio) e detalhes tecnológicos (infraestrutura, persistência e interface de usuário).

---

## Pré-requisitos

Para rodar e compilar este projeto localmente, certifique-se de possuir:

- **Java Development Kit (JDK)** versão 21 ou superior.
- **Apache Maven** versão 3.9+ (caso queira usar o Maven do sistema) ou o executor do Maven local disponível na pasta `.tools`.
- **PostgreSQL Database** versão 14 ou superior.
- **Python 3.x** (caso queira rodar a suíte de testes E2E).

---

## Como Configurar o Banco de Dados

O sistema utiliza o PostgreSQL para armazenar os dados de funcionários, histórico de cálculos e parâmetros da legislação tributária.

1. Acesse o terminal do seu PostgreSQL (`psql` ou via ferramenta gráfica como pgAdmin) e execute o script SQL abaixo para criar o banco de dados, o usuário e conceder as permissões necessárias:

   ```sql
   CREATE DATABASE folha;
   CREATE USER folha WITH PASSWORD 'folha';
   GRANT ALL PRIVILEGES ON DATABASE folha TO folha;
   ```

2. As migrações do banco de dados (Flyway) são ativadas automaticamente ao iniciar o aplicativo e irão estruturar todas as tabelas necessárias, além de inserir os dados iniciais de legislação para os anos de 2023 a 2025.

---

## Como Compilar o Projeto

Para compilar e gerar o arquivo executável compactado (JAR), utilize o utilitário do Maven local fornecido no projeto:

```bash
/home/mateus/Documents/dev/folha_de_pagamento_DANIEL/folha_de_pagamento_1/.tools/apache-maven-3.9.6/bin/mvn clean package
```

Este comando irá compilar os fontes, rodar os testes unitários e de integração e gerar o arquivo JAR final localizado em:
`target/folha-pagamento-pf-0.0.1-SNAPSHOT.jar`

---

## Como Rodar o JAR

Após compilar o projeto com sucesso, você poderá iniciar a aplicação e interagir com ela por meio do console de linha de comando:

```bash
java -jar target/folha-pagamento-pf-0.0.1-SNAPSHOT.jar
```

Uma vez iniciado o console do Spring Shell, digite o comando `wizard` para entrar no menu numerado interativo:

```bash
shell:> wizard
```

## Regimes Trabalhistas e Anos Suportados

O sistema foi modelado para suportar diversos regimes de contratação de acordo com a legislação e parâmetros vigentes no período de **2023 a 2025**:

1. **CLT (Consolidação das Leis do Trabalho)**:
   - Cálculo progressivo de INSS.
   - Cálculo progressivo de IRRF (com opção de dedução convencional por dependentes ou desconto simplificado, aplicando a opção mais vantajosa ao trabalhador).
   - Encargo de FGTS (8% pago pelo empregador, meramente informativo).
   - Suporte a adicionais de hora extra e adicional noturno.

2. **PJ (Pessoa Jurídica)**:
   - Recebe com base no valor do contrato.
   - Dedução opcional do Imposto sobre Serviços (ISS) com base em alíquota configurada.

3. **COMISSIONADO**:
   - Salário fixo acrescido de comissões variáveis transientes informadas no momento do cálculo.
   - Aplica os cálculos progressivos de INSS, IRRF e FGTS de maneira análoga ao regime CLT.

4. **ESTÁGIO**:
   - Bolsa-auxílio com auxílio transporte.
   - Isento de INSS e FGTS.
   - Sujeito a desconto de IRRF.

5. **JOVEM APRENDIZ**:
   - Salário proporcional.
   - Alíquota fixa de INSS de 5%.
   - Encargo de FGTS de 2% (informativo).
   - Sujeito a desconto de IRRF.

6. **RPA (Recibo de Pagamento a Autônomo)**:
   - Pagamento de serviço prestado de autônomos.
   - Desconto de INSS autônomo com base na alíquota selecionada (11% ou 20%), limitado ao teto do INSS.
   - Desconto de IRRF sobre a base líquida de INSS.
