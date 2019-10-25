package fr.unice.polytech.si5.al.creditrama.teamd.bankservice.controller;

import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.BankAccount;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.Client;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class ClientController {

    private ClientService clientService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public ClientController(ClientService clientService, PasswordEncoder passwordEncoder) {
        this.clientService = clientService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/clients")
    public List<Client> getUsers() {
        return clientService.fetchAllClient();
    }

    @GetMapping("/clients/{id}")
    public Client getUserById(@PathVariable int id) {
        return clientService.fetchById(id);
    }


    /**
     * This method do not respect REST pattern because we have to allow this route for everyone
     * So this route /register his not protected by auth
     * @param customer
     * @return
     */
    @PostMapping("/register")
    public Client addUser(@RequestBody Client customer) {
        customer.setAccountNonExpired(true);
        customer.setAccountNonLocked(true);
        customer.setCredentialsNonExpired(true);
        customer.setEnabled(true);
        customer.setPassword("{bcrypt}" + passwordEncoder.encode(customer.getPassword()));
        customer.setBankAccounts(new ArrayList<BankAccount>() {{
            add(new BankAccount(0, 100));
        }});
        return clientService.save(customer);
    }

    /**
     * return the current user according to his bearer token
     * @param authentication information
     * @return
     */
    @GetMapping("/clients/auth")
    public Client getAuthClientByName(Authentication authentication){
        return clientService.fetchByName(authentication.getName());
    }
}
