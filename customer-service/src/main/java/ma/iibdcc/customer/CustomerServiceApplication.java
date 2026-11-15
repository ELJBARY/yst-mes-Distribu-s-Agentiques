package ma.iibdcc.customer;

import ma.iibdcc.customer.entities.Customer;
import ma.iibdcc.customer.mcp.CustomerMcpTools;
import ma.iibdcc.customer.repository.CustomerRepository;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class CustomerServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

    /** Expose les méthodes @Tool via MCP. */
    @Bean
    List<ToolCallback> customerTools(CustomerMcpTools tools) {
        return List.of(ToolCallbacks.from(tools));
    }

    @Bean
    CommandLineRunner seed(CustomerRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(new Customer(null, "Hassan", "hassan@gmail.com"));
                repo.save(new Customer(null, "Yassine", "yassine@gmail.com"));
                repo.save(new Customer(null, "Fatima", "fatima@gmail.com"));
            }
        };
    }
}
