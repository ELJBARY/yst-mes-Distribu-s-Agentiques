# Architecture micro-services agentique (Spring Cloud + Spring AI + MCP)

| Service | Port | Rôle |
|---|---|---|
| discovery-service | 8761 | Eureka Server |
| config-service | 8888 | Spring Cloud Config (dossier `config-service/config-repo`) |
| customer-service | 8081 | CRUD clients (H2) + serveur MCP Streamable (`/mcp`) |
| ebank-service | 8082 | Comptes bancaires (H2), appelle customer-service via **OpenFeign** + **Resilience4J** (circuit breaker + fallback), serveur MCP (`/mcp`) |
| gateway-service | 8080 | Spring Cloud Gateway (`/api/customers`, `/api/accounts`, `/api/chat`) |
| chatbot-service | 8083 | Agent IA Spring AI (OpenAI) + client MCP vers customer/ebank + bot Telegram + page web `/` |

## Lancer
```bash
mvn clean package -DskipTests
export OPENAI_API_KEY=sk-...          # jamais dans le code
export TELEGRAM_BOT_TOKEN=...         # optionnel
# ordre : discovery, config, customer, ebank, gateway, chatbot
for s in discovery-service config-service customer-service ebank-service gateway-service chatbot-service; do
  (cd $s && java -jar target/$s-1.0.0.jar &) ; sleep 15; done
```
- Chat web : http://localhost:8083/  — API : `POST http://localhost:8080/api/chat {"question":"..."}`
- Eureka : http://localhost:8761 — Circuit breakers : http://localhost:8082/actuator/circuitbreakers
- Test du fallback : arrêter customer-service puis appeler `GET /api/accounts` (le client devient « Non disponible », le circuit passe à OPEN).
