package ma.iibdcc.chatbot;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service
public class AgentService {
    private final ChatClient agent;

    public AgentService(ChatClient agent) {
        this.agent = agent;
    }

    public String ask(String conversationId, String question) {
        return agent.prompt()
                .user(question)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }
}
