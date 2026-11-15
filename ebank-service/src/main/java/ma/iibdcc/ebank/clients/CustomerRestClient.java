package ma.iibdcc.ebank.clients;

import ma.iibdcc.ebank.model.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Appel de customer-service via Open Feign (résolution par Eureka + load balancing).
 * Le circuit breaker Resilience4J est activé sur le client (spring.cloud.openfeign.circuitbreaker.enabled)
 * et bascule sur {@link CustomerRestClientFallback} en cas d'erreur / circuit ouvert.
 */
@FeignClient(name = "CUSTOMER-SERVICE", fallback = CustomerRestClientFallback.class)
public interface CustomerRestClient {
    @GetMapping("/api/customers/{id}")
    Customer findCustomerById(@PathVariable("id") Long id);

    @GetMapping("/api/customers")
    List<Customer> allCustomers();
}
