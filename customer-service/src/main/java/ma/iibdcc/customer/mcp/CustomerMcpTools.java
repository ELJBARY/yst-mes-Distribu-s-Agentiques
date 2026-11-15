package ma.iibdcc.customer.mcp;

import ma.iibdcc.customer.entities.Customer;
import ma.iibdcc.customer.repository.CustomerRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerMcpTools {
    private final CustomerRepository repo;

    public CustomerMcpTools(CustomerRepository repo) {
        this.repo = repo;
    }

    @Tool(name = "listCustomers", description = "Retourne la liste de tous les clients de la banque")
    public List<Customer> listCustomers() {
        return repo.findAll();
    }

    @Tool(name = "getCustomerById", description = "Retourne un client à partir de son identifiant")
    public Customer getCustomerById(@ToolParam(description = "Identifiant du client") Long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Client introuvable : " + id));
    }

    @Tool(name = "searchCustomersByName", description = "Recherche des clients dont le nom contient un texte")
    public List<Customer> searchCustomersByName(@ToolParam(description = "Texte à chercher dans le nom") String name) {
        return repo.findByNameContainingIgnoreCase(name);
    }

    @Tool(name = "createCustomer", description = "Crée un nouveau client")
    public Customer createCustomer(@ToolParam(description = "Nom du client") String name,
                                   @ToolParam(description = "Email du client") String email) {
        return repo.save(new Customer(null, name, email));
    }
}
