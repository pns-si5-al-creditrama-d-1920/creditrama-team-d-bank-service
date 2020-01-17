package fr.unice.polytech.si5.al.creditrama.teamd.clientservice.service;

import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.exception.BankAccountNotFoundException;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.exception.ClientNotFoundException;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.BankAccount;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.entity.Client;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClientService {

    Client save(Client customer);

    Client fetchById(long profileId) throws ClientNotFoundException;

    Client fetchByName(String name);

    List<Client> fetchAllClient();

    void deleteById(long userId);

    void createAccount(long id) throws ClientNotFoundException;

    BankAccount addRecipient(long id, String iban) throws ClientNotFoundException, BankAccountNotFoundException;

    void removeRecipient(long clientId, String recipientId) throws ClientNotFoundException;

    void initAdmin(String password);
}