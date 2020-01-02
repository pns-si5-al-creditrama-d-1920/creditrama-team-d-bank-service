package fr.unice.polytech.si5.al.creditrama.teamd.bankservice.service;

import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.Bank;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.BankAccount;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.Client;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.repository.ClientRepository;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {


    private ClientRepository customerRepository;

    private PasswordEncoder passwordEncoder;

    private BankService bankService;

    @Autowired
    public ClientServiceImpl(ClientRepository customerRepository, PasswordEncoder passwordEncoder, BankService bankService) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.bankService = bankService;
    }

    @Override
    public Client save(Client customer) {
        customer.setAccountNonExpired(true);
        customer.setAccountNonLocked(true);
        customer.setCredentialsNonExpired(true);
        customer.setEnabled(true);
        customer.setPassword("{bcrypt}" + passwordEncoder.encode(customer.getPassword()));
        customer.getBankAccounts().add(BankAccount.builder().balance(100.0).build());
        Bank bank = bankService.getCurrentBank().get();
        customer.setBank(bank);
        Iban iban = new Iban.Builder()
                .countryCode(CountryCode.valueOf(bank.getCountryCode()))
                .bankCode(Long.toString(bank.getBankCode()))
                .buildRandom();
        customer.setIban(iban.toString());
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
    public Client fetchByName(String name) {
        return customerRepository.findByUsername(name);
    }

    @Override
    public List<Client> fetchAllClient() {
        return customerRepository.findAll();
    }

    @Override
    public void deleteById(int userId) {
        customerRepository.deleteById(userId);
    }
}