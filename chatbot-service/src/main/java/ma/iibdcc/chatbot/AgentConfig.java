package ma.iibdcc.chatbot;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AgentConfig {

    @Bean
    ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder().maxMessages(20).build();
    }

    /**
     * Agent IA : LLM (OpenAI) + outils MCP exposés par customer-service et ebank-service
     * (le ToolCallbackProvider est fourni par le client MCP de Spring AI).
     */
    @Bean
    ChatClient agent(ChatModel chatModel, ToolCallbackProvider mcpTools, ChatMemory memory,
                     @Value("${chatbot.system-prompt:Tu es un assistant bancaire.}") String systemPrompt) {
        return ChatClient.builder(chatModel)
                .defaultSystem(systemPrompt)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(memory).build())
                .defaultToolCallbacks(mcpTools)
                .build();
    }
}
