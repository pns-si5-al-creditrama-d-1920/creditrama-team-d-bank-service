package fr.unice.polytech.si5.al.creditrama.teamd.bankservice.controller;

import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.business.IBankBusiness;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.exception.BankAccountNotFoundException;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.exception.ClientNotFoundException;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.exception.InvalidBankTransactionException;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.BankAccount;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.BankTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/bank")
public class BankController {
    @Autowired
    private IBankBusiness bankBusiness;

    @GetMapping("/clients/{id}/bank-accounts")
    public ResponseEntity<List<BankAccount>> getMyBankAccounts(@PathVariable(value = "id") Integer clientId) {
        try {
            return new ResponseEntity<>(bankBusiness.retrieveClientBankAccounts(clientId), HttpStatus.OK);
        } catch (ClientNotFoundException e) {
            System.err.println("GET /bank/clients/{id}/bank-accounts : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/clients/{id}/bank-accounts")
    public ResponseEntity<BankAccount> createBankAccount(@PathVariable(value = "id") Integer clientId,
                                                         @RequestBody BankAccount bankAccount) {
        try {
            BankAccount createdBankAccount = bankBusiness.createClientBankAccount(clientId, bankAccount);
            bankBusiness.sendGreeting(clientId);
            return new ResponseEntity<>(createdBankAccount, HttpStatus.CREATED);
        } catch (ClientNotFoundException e) {
            System.err.println("POST /bank/clients/{id}/bank-accounts : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/clients/{id}/recipients")
    public ResponseEntity<List<BankAccount>> getRecipients(@PathVariable(value = "id") Integer clientId) {
        try {
            return new ResponseEntity<>(bankBusiness.retrieveClientRecipients(clientId), HttpStatus.OK);
        } catch (ClientNotFoundException e) {
            System.err.println("GET /bank/clients/{id}/recipients : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/clients/{id}/recipients")
    public ResponseEntity<BankAccount> addRecipient(@PathVariable(value = "id") Integer clientId, @RequestBody BankAccount recipientBankAccount) {
        try {
            return new ResponseEntity<>(bankBusiness.createClientRecipient(clientId, recipientBankAccount), HttpStatus.CREATED);
        } catch (ClientNotFoundException | BankAccountNotFoundException e) {
            System.err.println("POST /bank/clients/{id}/recipients : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/clients/{id}/transactions")
    public ResponseEntity<BankTransaction> transfer(@PathVariable(value = "id") Integer clientId, @RequestBody BankTransaction transaction) {
        try {
            return new ResponseEntity<>(bankBusiness.createClientTransaction(clientId, transaction), HttpStatus.CREATED);
        } catch (ClientNotFoundException | BankAccountNotFoundException e) {
            System.err.println("POST /bank/clients/{id}/transactions : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (InvalidBankTransactionException e) {
            System.err.println("POST /bank/clients/{id}/transactions : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
    }
}
