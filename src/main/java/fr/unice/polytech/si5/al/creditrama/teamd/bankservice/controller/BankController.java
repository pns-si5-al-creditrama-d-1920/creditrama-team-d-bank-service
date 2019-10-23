package fr.unice.polytech.si5.al.creditrama.teamd.bankservice.controller;

import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.BankAccount;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.Client;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.BankTransaction;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.repository.BankAccountRepository;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.repository.ClientRepository;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("bank")
public class BankController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private BankAccountRepository bankAccountRepository;

    private final NotificationService notificationService;

    public BankController(ClientRepository clientRepository, NotificationService notificationService, BankAccountRepository bankAccountRepository) {
        this.clientRepository = clientRepository;
        this.notificationService = notificationService;
        this.bankAccountRepository = bankAccountRepository;
    }

    @GetMapping("/clients/{id}/bank-accounts")
    public ResponseEntity<List<BankAccount>> getMyBankAccounts(@PathVariable(value = "id") Integer clientId) {
        Optional<Client> user = clientRepository.findById(clientId);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get().getBankAccounts(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/clients/{id}/bank-accounts")
    public ResponseEntity<BankAccount> createBankAccount(@PathVariable(value = "id") Integer id, @RequestBody BankAccount account) {
        Optional<Client> user = clientRepository.findById(id);
        if (user.isPresent()) {
            user.get().getBankAccounts().add(account);
            this.notificationService.sendGreeting("{ \"email\": \""+user.get().getEmail()+"\" }");
            clientRepository.save(user.get());
            return new ResponseEntity<>(account, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/clients/{id}/recipients")
    public ResponseEntity<BankAccount> addRecipient(@PathVariable(value = "id") Integer userId, @RequestBody BankAccount recipientBankAccount) {
        Optional<Client> user = clientRepository.findById(userId);
        Optional<BankAccount> bankAccount = bankAccountRepository.findById(recipientBankAccount.getBankAccountId());
        if (user.isPresent() && bankAccount.isPresent()
                && !user.get().getRecipients().contains(bankAccount.get())
                && !user.get().getBankAccounts().contains(bankAccount.get())) {
            user.get().getRecipients().add(bankAccount.get());
            clientRepository.save(user.get());
            return new ResponseEntity<>(bankAccount.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/clients/{id}/recipients")
    public ResponseEntity<List<BankAccount>> getRecipients(@PathVariable(value = "id")Integer clientId) {
        Optional<Client> user = clientRepository.findById(clientId);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get().getRecipients(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/clients/{id}/transactions")
    public ResponseEntity<BankTransaction> transfer(@PathVariable(value = "id")Integer userId, @RequestBody BankTransaction transaction) {
        Optional<Client> client = clientRepository.findById(userId);
        Optional<BankAccount> clientBankAccount = bankAccountRepository.findById(transaction.getSourceId());
        Optional<BankAccount> destinationAccount = bankAccountRepository.findById(transaction.getDestinationId());
        if (client.isPresent() && clientBankAccount.isPresent() && destinationAccount.isPresent()
        && client.get().getBankAccounts().contains(clientBankAccount.get())
        && client.get().getRecipients().contains(destinationAccount.get())) {
            int previousBalanceSender = clientBankAccount.get().getBalance();
            int previousBalanceReceiver = destinationAccount.get().getBalance();
            clientBankAccount.get().setBalance(previousBalanceSender - transaction.getAmount());
            destinationAccount.get().setBalance(previousBalanceReceiver + transaction.getAmount());
            // add transactions for receiver and sender
            client.get().getTransactions().add(transaction);
            Client recipientClient = clientRepository.findByBankAccounts(destinationAccount.get());
            if (recipientClient == null) {
                System.out.println("No recipient client");
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            recipientClient.getTransactions().add(transaction);
            // save bank accounts
            bankAccountRepository.save(clientBankAccount.get());
            bankAccountRepository.save(destinationAccount.get());
            // save clients
            clientRepository.save(client.get());
            clientRepository.save(recipientClient);
            return new ResponseEntity<>(transaction, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}
