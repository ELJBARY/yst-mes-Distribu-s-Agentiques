package ma.iibdcc.ebank.service;

import ma.iibdcc.ebank.clients.CustomerRestClient;
import ma.iibdcc.ebank.entities.AccountType;
import ma.iibdcc.ebank.entities.BankAccount;
import ma.iibdcc.ebank.repository.BankAccountRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class AccountService {
    private final BankAccountRepository repo;
    private final CustomerRestClient customers;

    public AccountService(BankAccountRepository repo, CustomerRestClient customers) {
        this.repo = repo;
        this.customers = customers;
    }

    public List<BankAccount> findAll() {
        List<BankAccount> accounts = repo.findAll();
        accounts.forEach(this::enrich);
        return accounts;
    }

    public BankAccount findById(String id) {
        BankAccount a = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Compte introuvable : " + id));
        return enrich(a);
    }

    public List<BankAccount> findByCustomer(Long customerId) {
        List<BankAccount> accounts = repo.findByCustomerId(customerId);
        accounts.forEach(this::enrich);
        return accounts;
    }

    public BankAccount create(double balance, String currency, AccountType type, Long customerId) {
        // Vérifie que le client existe (appel Feign ; en cas d'exception 404 on refuse la création)
        var customer = customers.findCustomerById(customerId);
        if (customer == null || "Non disponible".equals(customer.name())) {
            throw new IllegalStateException("Client " + customerId + " indisponible ou inexistant");
        }
        BankAccount a = new BankAccount();
        a.setId(UUID.randomUUID().toString());
        a.setBalance(balance);
        a.setCurrency(currency == null ? "MAD" : currency);
        a.setType(type);
        a.setCreatedAt(LocalDate.now());
        a.setCustomerId(customerId);
        repo.save(a);
        a.setCustomer(customer);
        return a;
    }

    private BankAccount enrich(BankAccount a) {
        a.setCustomer(customers.findCustomerById(a.getCustomerId()));
        return a;
    }
}
