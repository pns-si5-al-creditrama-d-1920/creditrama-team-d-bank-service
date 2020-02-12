package fr.unice.polytech.si5.al.creditrama.teamd.clientservice.controller;

import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.exception.ClientNotFoundException;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.Card;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "content-type")
@RestController
public class CardController {
    private final ClientService clientService;

    @Autowired
    public CardController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/clients/{id}/cards")
    public ResponseEntity<List<Card>> getCardsOfClient(@PathVariable(value = "id") Long clientId) {
        try {
            return ResponseEntity.ok(clientService.getCardsOfClient(clientId));
        } catch (ClientNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
