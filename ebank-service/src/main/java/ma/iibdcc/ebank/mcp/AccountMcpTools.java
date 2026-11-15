package ma.iibdcc.ebank.mcp;

import ma.iibdcc.ebank.entities.AccountType;
import ma.iibdcc.ebank.entities.BankAccount;
import ma.iibdcc.ebank.service.AccountService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountMcpTools {
    private final AccountService service;

    public AccountMcpTools(AccountService service) {
        this.service = service;
    }

    @Tool(name = "listAccounts", description = "Retourne la liste de tous les comptes bancaires avec leur propriétaire")
    public List<BankAccount> listAccounts() {
        return service.findAll();
    }

    @Tool(name = "getAccountById", description = "Retourne les détails d'un compte bancaire à partir de son identifiant (UUID)")
    public BankAccount getAccountById(@ToolParam(description = "Identifiant UUID du compte") String id) {
        return service.findById(id);
    }

    @Tool(name = "getAccountsByCustomer", description = "Retourne les comptes bancaires d'un client")
    public List<BankAccount> getAccountsByCustomer(@ToolParam(description = "Identifiant du client") Long customerId) {
        return service.findByCustomer(customerId);
    }

    @Tool(name = "createAccount", description = "Crée un compte bancaire pour un client existant")
    public BankAccount createAccount(@ToolParam(description = "Solde initial") double balance,
                                     @ToolParam(description = "Type : CURRENT_ACCOUNT ou SAVING_ACCOUNT") AccountType type,
                                     @ToolParam(description = "Identifiant du client propriétaire") Long customerId) {
        return service.create(balance, "MAD", type, customerId);
    }
}
