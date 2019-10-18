package fr.unice.polytech.si5.al.creditrama.teamd.bankservice.service;

import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.Client;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    ClientRepository customerRepository;

    @Override
    public Client save(Client customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Client fetchById(int profileId) {
        Optional<Client> customer = customerRepository.findById(profileId);
        if (customer.isPresent()) {
            return customer.get();
        } else {
            return null;
        }
    }

    @Override
    public List<Client> fetchAllProfiles() {
        return customerRepository.findAll();
    }
}