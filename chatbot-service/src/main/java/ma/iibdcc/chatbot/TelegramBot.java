package ma.iibdcc.chatbot;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

/**
 * Passerelle Telegram : long polling sur l'API Telegram, chaque message est transmis à l'agent IA
 * et la réponse est renvoyée dans la même conversation. Actif seulement si TELEGRAM_BOT_TOKEN est défini.
 */
@Component
@ConditionalOnExpression("!'${telegram.bot-token:}'.isEmpty()")
public class TelegramBot {
    private static final Logger log = LoggerFactory.getLogger(TelegramBot.class);

    private final AgentService agent;
    private final RestClient http;

    public TelegramBot(AgentService agent, @Value("${telegram.bot-token}") String token) {
        this.agent = agent;
        this.http = RestClient.builder().baseUrl("https://api.telegram.org/bot" + token).build();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        Thread t = new Thread(this::poll, "telegram-poller");
        t.setDaemon(true);
        t.start();
        log.info("Telegram bot démarré (long polling)");
    }

    private void poll() {
        long offset = 0;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                JsonNode res = http.get().uri("/getUpdates?timeout=30&offset={o}", offset)
                        .retrieve().body(JsonNode.class);
                if (res == null) continue;
                for (JsonNode u : res.path("result")) {
                    offset = u.path("update_id").asLong() + 1;
                    JsonNode msg = u.path("message");
                    if (!msg.hasNonNull("text")) continue;
                    long chatId = msg.path("chat").path("id").asLong();
                    String answer;
                    try {
                        answer = agent.ask("telegram-" + chatId, msg.path("text").asText());
                    } catch (Exception e) {
                        answer = "Désolé, une erreur est survenue : " + e.getMessage();
                    }
                    http.post().uri("/sendMessage").body(Map.of("chat_id", chatId, "text", answer))
                            .retrieve().toBodilessEntity();
                }
            } catch (Exception e) {
                log.warn("Telegram polling : {}", e.getMessage());
                try { Thread.sleep(5000); } catch (InterruptedException ie) { return; }
            }
        }
    }
}
