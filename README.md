# Sistema de Controle de Expedicao de Materiais (MVP)

API RESTful em **Java 17 + Spring Boot 3.3.x** para cadastro de materiais e controle
do fluxo de expedicao, com baixa automatica de estoque na conclusao.

## Stack

- Spring Web, Spring Data JPA, Bean Validation, Lombok
- Banco H2 em memoria (`create-drop`)

## Como executar

```bash
mvn spring-boot:run
```

Aplicacao sobe em `http://localhost:8080`.
Console H2: `http://localhost:8080/h2-console` (JDBC URL `jdbc:h2:mem:expedicaodb`, user `sa`, sem senha).

## Estrutura de pacotes

```
com.expedicao.controle
├── domain            entidades JPA + enums
├── repository        Spring Data JPA
├── dto               objetos de entrada/saida (records)
├── service           regras de negocio
├── controller        endpoints REST
└── exception         excecoes de dominio + GlobalExceptionHandler
```

## Endpoints

### Materiais
| Metodo | Rota | Descricao |
|--------|------|-----------|
| POST   | `/api/materiais` | Cria material (201 + Location) |
| GET    | `/api/materiais` | Lista materiais |
| GET    | `/api/materiais/{id}` | Busca por id |
| PATCH  | `/api/materiais/{id}/estoque` | Ajuste absoluto de saldo |
| DELETE | `/api/materiais/{id}` | Remove material sem expedicao PENDENTE (204) |

### Expedicoes
| Metodo | Rota | Descricao |
|--------|------|-----------|
| POST   | `/api/expedicoes` | Cria expedicao (nasce PENDENTE) |
| GET    | `/api/expedicoes?status=PENDENTE` | Lista, com filtro opcional de status |
| GET    | `/api/expedicoes/{id}` | Busca por id |
| PATCH  | `/api/expedicoes/{id}/conclusao` | Confirma e da baixa no estoque |
| PATCH  | `/api/expedicoes/{id}/cancelamento` | Cancela expedicao PENDENTE |

## Regras de negocio

- SKU de material e unico.
- Expedicao sempre nasce no status `PENDENTE`.
- A baixa de estoque ocorre **somente na conclusao**; a validacao feita na criacao
  e um *fail-fast* de UX e nao reserva saldo.
- Conclusao e cancelamento so sao permitidos a partir de `PENDENTE`.
- Estoque insuficiente na conclusao retorna `422 Unprocessable Entity`.
- Material com expedicao `PENDENTE` nao pode ser excluido (`409 Conflict`).
- `@Version` em `Material` protege a baixa contra concorrencia (`409` em conflito).

## Exemplos rapidos (cURL)

```bash
# criar material
curl -X POST http://localhost:8080/api/materiais \
  -H "Content-Type: application/json" \
  -d '{"nome":"Parafuso M8","sku":"PAR-M8-001","quantidadeEstoque":100,"unidadeMedida":"UN"}'

# criar expedicao
curl -X POST http://localhost:8080/api/expedicoes \
  -H "Content-Type: application/json" \
  -d '{"materialId":1,"quantidadeExpedida":30,"destinatario":"Obra Zona Norte"}'

# concluir (baixa de estoque)
curl -X PATCH http://localhost:8080/api/expedicoes/1/conclusao
```
