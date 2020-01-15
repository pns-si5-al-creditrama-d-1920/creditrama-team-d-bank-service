package fr.unice.polytech.si5.al.creditrama.teamd.clientservice.controller;

import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.Client;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.repository.BankAccountRepository;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class DumpController {
    private BankAccountRepository bankAccountRepository;
    private ClientRepository clientRepository;

    @Autowired
    public DumpController(BankAccountRepository bankAccountRepository, ClientRepository clientRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.clientRepository = clientRepository;
    }

    @GetMapping("/dump")
    public ResponseEntity<List<Client>> instantPrettyDump() {
        return new ResponseEntity<>(this.clientRepository.findAll(), HttpStatus.OK);
    }
}
