package fr.unice.polytech.si5.al.creditrama.teamd.clientservice.service;

import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.client.BankAccountClient;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.exception.BankAccountNotFoundException;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.exception.ClientNotFoundException;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.exception.ErrorWhenCreatingClient;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.Bank;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.BankAccount;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.BankAccountRequest;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.Client;
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

    @Autowired
    public ClientServiceImpl(ClientRepository customerRepository, PasswordEncoder passwordEncoder, BankService bankService, BankAccountClient bankAccountClient) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.bankService = bankService;
        this.bankAccountClient = bankAccountClient;
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
        try{
            BankAccount account = bankAccountClient.createAccount(customer.getUserId(), BankAccountRequest.builder().amount(100d).build());
            customer.getBankAccounts().add(account.getIban());
            customer.setBank(bank);
            return customerRepository.save(customer);
        }catch (Exception e){
            customerRepository.deleteById(customer.getUserId());
            e.printStackTrace();
            throw new ErrorWhenCreatingClient();
        }
    }

    @Override
    public Client fetchById(int profileId) throws ClientNotFoundException {
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
    public void deleteById(int userId) {
        customerRepository.deleteById(userId);
    }

    @Override
    public void createAccount(int id) throws ClientNotFoundException {
        if (customerRepository.existsById(id))
            bankAccountClient.createAccount(id, BankAccountRequest.builder().amount(0).build());
        else
            throw new ClientNotFoundException("Client with id " + id + " not found");
    }

    @Override
    public BankAccount addRecipient(int id, String iban) throws ClientNotFoundException, BankAccountNotFoundException {
        Client client = fetchById(id);
        BankAccount bankAccountByIban = bankAccountClient.getBankAccount(iban);
        System.out.println(bankAccountByIban);
        if (bankAccountByIban != null && bankAccountByIban.getIban().equals(iban)) {
            client.getRecipients().add(iban);
            customerRepository.save(client);
            return bankAccountByIban;
        } else {
            throw new BankAccountNotFoundException("Bank account with iban " + iban + " not found.");
        }
    }

    /**
     * @Override public void addRecipient(int id, String iban) throws ClientNotFoundException, BankAccountNotFoundException {
     * Client client = fetchById(id);
     * Optional<BankAccount> bankAccountByIban = bankAccountClient.getBankAccountByIban(iban);
     * if (bankAccountByIban.isPresent()) {
     * client.getRecipients().add(bankAccountByIban.get());
     * customerRepository.save(client);
     * } else {
     * throw new BankAccountNotFoundException("Bank account with iban " + iban + " not found.");
     * }
     * }
     **/

    @Override
    public void removeRecipient(Integer clientId, String recipientId) throws ClientNotFoundException {
        Client client = fetchById(clientId);
        client.getRecipients().removeIf(v -> v.equals(recipientId));
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
            customer.setPassword("{bcrypt}" + passwordEncoder.encode(password));
            Bank bank = bankService.getCurrentBank().get();
            customer.setBank(bank);
            customer.setUsername("admin");
            customerRepository.save(customer);
        }
    }
}