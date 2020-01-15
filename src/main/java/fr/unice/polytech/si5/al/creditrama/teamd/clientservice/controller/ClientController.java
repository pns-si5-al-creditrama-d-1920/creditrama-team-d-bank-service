package fr.unice.polytech.si5.al.creditrama.teamd.clientservice.controller;

import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.exception.ClientNotFoundException;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.Client;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class ClientController {

    private ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/clients")
    public List<Client> getUsers() {
        return clientService.fetchAllClient();
    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<Client> getUserById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(clientService.fetchById(id));
        } catch (ClientNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/clients/{id}")
    public void deleteUserById(@PathVariable int id) {
        clientService.deleteById(id);
    }

    /**
     * This method do not respect REST pattern because we have to allow this route for everyone
     * So this route /register his not protected by auth
     *
     * @param customer
     * @return
     */
    @PostMapping("/register")
    public Client addUser(@RequestBody Client customer) {
        return clientService.save(customer);
    }

    /**
     * return the current user according to his bearer token
     *
     * @param authentication information
     * @return
     */
    @GetMapping("/clients/auth")
    public Client getAuthClientByName(Authentication authentication) {
        return clientService.fetchByName(authentication.getName());
    }
}
