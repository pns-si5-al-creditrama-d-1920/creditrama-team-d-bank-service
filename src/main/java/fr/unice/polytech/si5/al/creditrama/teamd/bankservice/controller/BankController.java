package fr.unice.polytech.si5.al.creditrama.teamd.bankservice.controller;

import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.BankAccount;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.Client;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.repository.ClientRepository;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/bank")
public class BankController {
    @Autowired
    private ClientRepository clientRepository;

    private final NotificationService notificationService;

    public BankController(ClientRepository clientRepository, NotificationService notificationService) {
        this.clientRepository = clientRepository;
        this.notificationService = notificationService;
    }

    @GetMapping("/{id}/bank/accounts")
    public ResponseEntity<List<BankAccount>> getMyBankAccounts(@PathVariable(value = "id") Integer id) {
        Optional<Client> user = clientRepository.findById(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get().getBankAccounts(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{id}/bank/accounts")
    public ResponseEntity<BankAccount> createBankAccount(@PathVariable(value = "id") Integer id) {
        Optional<Client> user = clientRepository.findById(id);
        if (user.isPresent()) {
            this.notificationService.sendGreeting("{ \"email\": \"theo.foray@gmail.com\" }");
            BankAccount bankAccount = new BankAccount();
            bankAccount.setBalance(0);
            user.get().getBankAccounts().add(bankAccount);
            clientRepository.save(user.get());
            return new ResponseEntity<>(bankAccount, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}
