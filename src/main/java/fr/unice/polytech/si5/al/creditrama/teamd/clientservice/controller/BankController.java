package fr.unice.polytech.si5.al.creditrama.teamd.clientservice.controller;

import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.client.CardClient;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.exception.BankAccountNotFoundException;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.exception.ClientNotFoundException;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.BankAccount;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.BankAccountInformation;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.entity.Client;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.entity.RecipientAccount;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "content-type")
@RestController
public class BankController {
    private final ClientService clientService;
    private final CardClient cardClient;

    @Autowired
    public BankController(ClientService clientService, CardClient cardClient) {
        this.clientService = clientService;
        this.cardClient = cardClient;
    }

    @PostMapping("/clients/{id}/bank-accounts")
    public ResponseEntity<Void> createBankAccount(@PathVariable(value = "id") Integer clientId) {
        try {
            BankAccount createdAccount = clientService.createAccount(clientId);
            Client client = clientService.fetchById(clientId);
            cardClient.createCard(BankAccountInformation.builder()
                    .iban(createdAccount.getIban())
                    .firstName(client.getFirstName())
                    .lastName(client.getLastName())
                    .build());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (ClientNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/clients/{id}/recipients")
    public ResponseEntity<BankAccount> addRecipient(@PathVariable(value = "id") int clientId, @RequestBody String recipientBankAccountId) {
        try {
            return new ResponseEntity<>(clientService.addRecipient(clientId, recipientBankAccountId), HttpStatus.CREATED);
        } catch (ClientNotFoundException | BankAccountNotFoundException e) {
            System.err.println("POST /bank/clients/{id}/recipients : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/clients/{id}/recipients")
    public List<RecipientAccount> getRecipient(@PathVariable(value = "id") int clientId) throws ClientNotFoundException {
        return clientService.fetchById(clientId).getRecipients();
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
