package fr.unice.polytech.si5.al.creditrama.teamd.bankservice.controller;

import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.exception.BankAccountNotFoundException;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.exception.ClientNotFoundException;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "content-type")
@RestController
@RequestMapping("/bank")
public class BankController {
    private final ClientService clientService;

    @Autowired
    public BankController(ClientService clientService) {
        this.clientService = clientService;
    }


    @PostMapping("/clients/{id}/bank-accounts")
    public ResponseEntity<Void> createBankAccount(@PathVariable(value = "id") Integer clientId) {
        try {
            clientService.createAccount(clientId);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (ClientNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/clients/{id}/recipients")
    public ResponseEntity<Void> addRecipient(@PathVariable(value = "id") int clientId, @RequestBody String recipientBankAccountId) {
        try {
            clientService.addRecipient(clientId, recipientBankAccountId);
            return ResponseEntity.ok().build();
        } catch (ClientNotFoundException | BankAccountNotFoundException e) {
            System.err.println("POST /bank/clients/{id}/recipients : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/clients/{clientId}/recipients/{recipientId}")
    public ResponseEntity deleteRecipient(@PathVariable(value = "clientId") Integer clientId, @PathVariable(value = "recipientId") String recipientId) {
        try {
            clientService.removeRecipient(clientId, recipientId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ClientNotFoundException e) {
            System.err.println("POST /bank/clients/{id}/recipients : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
