package fr.unice.polytech.si5.al.creditrama.teamd.clientservice.service;

import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.client.BankAccountClient;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.client.CardClient;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.exception.BankAccountNotFoundException;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.exception.ClientNotFoundException;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.exception.ErrorWhenCreatingClient;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.*;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.entity.Client;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.entity.RecipientAccount;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.repository.client.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {


    private ClientRepository customerRepository;

    private PasswordEncoder passwordEncoder;

    private BankService bankService;

    private BankAccountClient bankAccountClient;

    private CardClient cardClient;

    @Autowired
    public ClientServiceImpl(ClientRepository customerRepository, PasswordEncoder passwordEncoder, BankService bankService, BankAccountClient bankAccountClient, CardClient cardClient) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.bankService = bankService;
        this.bankAccountClient = bankAccountClient;
        this.cardClient = cardClient;
    }

    @Override
    public Client save(Client customer) {
        customer.setAccountNonExpired(true);
        customer.setAccountNonLocked(true);
        customer.setCredentialsNonExpired(true);
        customer.setEnabled(true);
        customer.setPassword("{bcrypt}" + passwordEncoder.encode(customer.getPassword()));
        Bank bank = bankService.getCurrentBank().get();
        customer.setBank(bank);
        customer = customerRepository.save(customer);
        try {
            BankAccount account = bankAccountClient.createAccount(customer.getUserId(), BankAccountRequest.builder().amount(100d).build());
            customer.getBankAccounts().add(account.getIban());
            customer.setBank(bank);
            Card card = cardClient.createCard(BankAccountInformation.builder()
                    .iban(account.getIban())
                    .firstName(customer.getFirstName())
                    .lastName(customer.getLastName())
                    .build());
            bankAccountClient.addCard(account.getIban(), CardRequest.builder().number(card.getNumber()).build());
            return customerRepository.save(customer);
        } catch (Exception e) {
            customerRepository.deleteById(customer.getUserId());
            e.printStackTrace();
            throw new ErrorWhenCreatingClient();
        }
    }

    @Override
    public Client fetchById(long profileId) throws ClientNotFoundException {
        Optional<Client> customer = customerRepository.findById(profileId);
        if (customer.isPresent()) {
            return customer.get();
        } else {
            throw new ClientNotFoundException("Client with id " + profileId + " not found");
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
    public void deleteById(long userId) {
        customerRepository.deleteById(userId);
    }

    @Override
    public BankAccount createAccount(long id) throws ClientNotFoundException {
        if (customerRepository.existsById(id))
            return bankAccountClient.createAccount(id, BankAccountRequest.builder().amount(0).build());
        else
            throw new ClientNotFoundException("Client with id " + id + " not found");
    }

    @Override
    public BankAccount addRecipient(long id, String iban) throws ClientNotFoundException, BankAccountNotFoundException {
        Client client = fetchById(id);
        BankAccount bankAccountByIban = bankAccountClient.getBankAccount(iban);
        Client recipient = fetchById(bankAccountByIban.getClient());
        if (bankAccountByIban.getIban().equals(iban)) {
            client.getRecipients().add(RecipientAccount.builder()
                    .client(bankAccountByIban.getClient())
                    .firstName(recipient.getFirstName())
                    .lastName(recipient.getLastName())
                    .iban(bankAccountByIban.getIban())
                    .build());
            customerRepository.save(client);
            return bankAccountByIban;
        } else {
            throw new BankAccountNotFoundException("Bank account with iban " + iban + " not found.");
        }
    }

    @Override
    public void removeRecipient(long clientId, String iban) throws ClientNotFoundException {
        Client client = fetchById(clientId);
        client.getRecipients().removeIf(v -> v.getIban().equals(iban));
        customerRepository.save(client);
    }

    @Override
    public void initAdmin(String password) {
        if (!customerRepository.existsByUsername("admin")) {
            Client customer = new Client();
            customer.setEmail("admin@creditrama.com");
            customer.setAccountNonExpired(true);
            customer.setAccountNonLocked(true);
            customer.setCredentialsNonExpired(true);
            customer.setEnabled(true);
            customer.setFirstName("admin");
            customer.setLastName("admin");
            customer.setPassword("{bcrypt}" + passwordEncoder.encode(password));
            Bank bank = bankService.getCurrentBank().get();
            customer.setBank(bank);
            customer.setUsername("admin");
            customerRepository.save(customer);
        }
    }
}