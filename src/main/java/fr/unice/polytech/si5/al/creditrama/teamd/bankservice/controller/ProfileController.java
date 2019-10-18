package fr.unice.polytech.si5.al.creditrama.teamd.bankservice.controller;

import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.Client;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class ProfileController {


    @Autowired
    ClientService customerService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public Client save(@RequestBody Client customer) {
        customer.setAccountNonExpired(true);
        customer.setAccountNonLocked(true);
        customer.setCredentialsNonExpired(true);
        customer.setEnabled(true);
        customer.setPassword("{bcrypt}"+passwordEncoder.encode(customer.getPassword()));
        return customerService.save(customer);
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public Client fetch(@RequestParam int profileId) {
        return customerService.fetchById(profileId);
    }

    @RequestMapping(value = "/profiles", method = RequestMethod.GET)
    public List<Client> fetch() {
        return customerService.fetchAllProfiles();
    }
}