# Desafio QA Automation Engineer - Accenture

Automação de testes de **API** e **Frontend** do site [demoqa.com](https://demoqa.com), escrita em **Java** com **BDD (Cucumber)**.
Todos os cenários (API + UI) rodam de forma contínua em **uma única execução**.

## Stack

| Ferramenta | Uso |
|---|---|
| Java 17 + Maven | Linguagem e build |
| Cucumber 7 (Gherkin em português) | BDD |
| JUnit 5 Platform Suite | Runner |
| Selenium WebDriver 4 | Automação da UI (o Selenium Manager baixa o ChromeDriver automaticamente) |
| RestAssured | Automação da API |
| AssertJ | Asserções |
| Datafaker | Massa de dados aleatória |
| PicoContainer | Injeção de dependência / compartilhamento de estado entre steps |

## Pré-requisitos

- JDK 17+
- Maven 3.8+
- Google Chrome

## Como executar

```bash
# Tudo (API + UI) em uma única execução
mvn test

# Sem abrir o navegador (headless)
mvn test -Dheadless=true

# Apenas uma parte, filtrando por tag
mvn test -Dcucumber.filter.tags="@api"
mvn test -Dcucumber.filter.tags="@ui"
mvn test -Dcucumber.filter.tags="@webtables"
mvn test -Dcucumber.filter.tags="@bonus"
```

Relatório HTML e Evidências gerados em:
- **Relatório HTML**: `target/cucumber-report/index.html` (com screenshots de cada passo anexados e screenshot de falha).
- **Evidências em Imagens**: `target/evidencias/<nome_do_cenario>/passo_XX.png` (galeria de fotos em disco de cada interação realizada).

## Cenários cobertos

### Parte 1 - API (`@api`)
`features/api/bookstore.feature`
1. Criar usuário — `POST /Account/v1/User`
2. Gerar token — `POST /Account/v1/GenerateToken`
3. Confirmar autorização — `POST /Account/v1/Authorized`
4. Listar livros — `GET /BookStore/v1/Books`
5. Alugar 2 livros aleatórios — `POST /BookStore/v1/Books`
6. Listar detalhes do usuário com os livros — `GET /Account/v1/User/{userID}`

Ao final o usuário criado é removido (`DELETE`) para não deixar lixo na base pública.

### Parte 2 - Frontend (`@ui`)
| Tag | Feature | O que valida |
|---|---|---|
| `@forms` | Practice Form | Preenche todo o formulário com dados aleatórios, faz upload de [`upload.txt`](src/test/resources/files/upload.txt), submete, valida o popup (e os dados exibidos) e o fecha |
| `@windows` | Browser Windows | Abre nova janela, valida a mensagem *"This is a sample page"* e fecha |
| `@webtables` | Web Tables | Cria, edita e deleta um registro |
| `@bonus` | Web Tables | Cria **12 registros dinamicamente** (Esquema do Cenário) e deleta todos |
| `@progressbar` | Progress Bar | Para antes de 25%, valida `<= 25%`, completa até 100% e reseta |
| `@sortable` | Sortable | Desordena a lista e a coloca em ordem crescente via drag and drop |

## Estrutura do projeto

```
src/test
├── java/com/accenture/desafio
│   ├── api
│   │   ├── client/        # Clients RestAssured (Account, BookStore)
│   │   ├── model/         # Records de request/response
│   │   └── ApiContext     # Estado compartilhado do fluxo de API
│   ├── hooks/             # Evidências passo a passo (@AfterStep) + screenshot em falha + teardown
│   ├── model/             # Modelos de dados da UI
│   ├── pages/             # Page Objects (BasePage + uma classe por página)
│   ├── runners/           # Runner único do Cucumber
│   ├── steps
│   │   ├── api/           # Steps da API
│   │   └── ui/            # Steps da UI
│   ├── support/           # DriverFactory e TestContext
│   └── utils/             # Geração de dados aleatórios
└── resources
    ├── features/api       # Features de API
    ├── features/ui        # Features de UI
    └── files/upload.txt   # Arquivo usado no upload
```

## Decisões técnicas

- **Page Object (POM)**: cada página tem sua classe com localizadores privados e métodos de negócio; `BasePage` concentra esperas explícitas, clique resiliente e remoção dos banners de anúncio do demoqa (que interceptam cliques).
- **Evidências Visuais Automáticas**: implementado hook `@AfterStep` no `Hooks.java` que captura screenshot após cada ação de UI, embutindo no relatório HTML oficial (`index.html`) e salvando as imagens em `target/evidencias/<cenario>/passo_XX.png`.
- **Slow Motion Configurável**: adicionado controle de cadência no `BasePage` (500ms por padrão com navegador aberto, 0ms em modo headless), permitindo visualização humana das ações. Pode ser customizado via `-DslowMotion=800` ou `-DslowMotion=0`.
- **Web Tables**: as linhas são identificadas pelo e-mail (único). A validação usa a caixa de busca; edição e exclusão são feitas sem filtro e com a maior quantidade de linhas por página, evitando problemas de paginação ao criar 12+ registros.
- **Progress Bar**: o monitoramento para interromper a barra roda dentro do navegador (JavaScript), garantindo precisão independente da latência do WebDriver.
- **Sortable**: a lista já carrega em ordem crescente; por isso o cenário primeiro a deixa em ordem decrescente (via drag and drop) e depois a reordena em ordem crescente.

## Bugs encontrados no demoqa durante a automação

| Página | Comportamento observado | Tratamento no teste |
|---|---|---|
| Practice Form | O botão **Close** do popup de confirmação não fecha o modal (nem com clique real) | Clica em Close e, se o popup continuar aberto, fecha com **ESC**; o step seguinte valida que o popup foi fechado |
| Web Tables | Os registros adicionados compartilham o mesmo id interno; **excluir um registro novo remove todos os registros novos** | Exclui apenas os registros que ainda existem; o step seguinte valida que nenhum dos registros criados permaneceu |
