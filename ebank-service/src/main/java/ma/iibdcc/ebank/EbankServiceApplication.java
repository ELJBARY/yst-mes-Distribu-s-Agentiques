package ma.iibdcc.ebank;

import ma.iibdcc.ebank.mcp.AccountMcpTools;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import ma.iibdcc.ebank.entities.AccountType;
import ma.iibdcc.ebank.repository.BankAccountRepository;
import ma.iibdcc.ebank.entities.BankAccount;
import org.springframework.boot.CommandLineRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
@EnableFeignClients
public class EbankServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EbankServiceApplication.class, args);
    }

    /** Expose les méthodes @Tool via MCP. */
    @Bean
    List<ToolCallback> accountTools(AccountMcpTools tools) {
        return List.of(ToolCallbacks.from(tools));
    }

    @Bean
    CommandLineRunner seed(BankAccountRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                for (long customerId = 1; customerId <= 3; customerId++) {
                    for (AccountType t : AccountType.values()) {
                        BankAccount a = new BankAccount();
                        a.setId(UUID.randomUUID().toString());
                        a.setBalance(1000 + Math.random() * 9000);
                        a.setCurrency("MAD");
                        a.setType(t);
                        a.setCreatedAt(LocalDate.now());
                        a.setCustomerId(customerId);
                        repo.save(a);
                    }
                }
            }
        };
    }
}
