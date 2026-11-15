package ma.iibdcc.chatbot;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin("*")
public class ChatRestController {
    public record ChatRequest(String question, String conversationId) {}

    private final AgentService agent;

    public ChatRestController(AgentService agent) {
        this.agent = agent;
    }

    @GetMapping
    public String chat(@RequestParam String question,
                       @RequestParam(defaultValue = "web") String conversationId) {
        return agent.ask(conversationId, question);
    }

    @PostMapping
    public String chatPost(@RequestBody ChatRequest r) {
        return agent.ask(r.conversationId() == null ? "web" : r.conversationId(), r.question());
    }
}
