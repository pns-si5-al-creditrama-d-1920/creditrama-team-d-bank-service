package fr.unice.polytech.si5.al.creditrama.teamd.bankservice.controller;

import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.Client;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private ClientRepository clientRepository;

    public UserController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping
    public ResponseEntity<List<Client>> getUsers() {
        List<Client> clients = new ArrayList<>();
        Iterable<Client> source = clientRepository.findAll();
        source.forEach(clients::add);
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Client> addUser(@RequestBody Client client) {
        this.clientRepository.save(client);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }
}
