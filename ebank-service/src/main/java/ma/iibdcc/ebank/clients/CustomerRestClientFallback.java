package ma.iibdcc.ebank.clients;

import ma.iibdcc.ebank.model.Customer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerRestClientFallback implements CustomerRestClient {
    @Override
    public Customer findCustomerById(Long id) {
        return new Customer(id, "Non disponible", "Non disponible");
    }

    @Override
    public List<Customer> allCustomers() {
        return List.of();
    }
}
