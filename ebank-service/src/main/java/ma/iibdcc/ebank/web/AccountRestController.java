package ma.iibdcc.ebank.web;

import ma.iibdcc.ebank.entities.AccountType;
import ma.iibdcc.ebank.entities.BankAccount;
import ma.iibdcc.ebank.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/accounts")
public class AccountRestController {
    public record CreateAccountRequest(double balance, String currency, AccountType type, Long customerId) {}

    private final AccountService service;

    public AccountRestController(AccountService service) {
        this.service = service;
    }

    @GetMapping
    public List<BankAccount> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public BankAccount byId(@PathVariable String id) {
        return service.findById(id);
    }

    @GetMapping("/customer/{customerId}")
    public List<BankAccount> byCustomer(@PathVariable Long customerId) {
        return service.findByCustomer(customerId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BankAccount create(@RequestBody CreateAccountRequest r) {
        return service.create(r.balance(), r.currency(), r.type(), r.customerId());
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFound(NoSuchElementException e) { return e.getMessage(); }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String unavailable(IllegalStateException e) { return e.getMessage(); }
}
