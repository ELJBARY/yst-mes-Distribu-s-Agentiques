package ma.iibdcc.ebank.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import ma.iibdcc.ebank.model.Customer;

import java.time.LocalDate;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BankAccount {
    @Id
    private String id;
    private double balance;
    private LocalDate createdAt;
    private String currency;
    @Enumerated(EnumType.STRING)
    private AccountType type;
    private Long customerId;
    /** Récupéré à la volée via OpenFeign, non persisté. */
    @Transient
    private Customer customer;

    public BankAccount() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public AccountType getType() { return type; }
    public void setType(AccountType type) { this.type = type; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
}
