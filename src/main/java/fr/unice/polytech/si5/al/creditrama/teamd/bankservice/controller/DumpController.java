package fr.unice.polytech.si5.al.creditrama.teamd.bankservice.controller;

import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.Client;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.repository.client.ClientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class DumpController {
    private ClientRepository clientRepository;

    public DumpController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping("/dump")
    public ResponseEntity<List<Client>> instantPrettyDump() {
        return new ResponseEntity<>(this.clientRepository.findAll(), HttpStatus.OK);
    }
}
