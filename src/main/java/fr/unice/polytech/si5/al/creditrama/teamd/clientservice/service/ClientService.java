
package fr.unice.polytech.si5.al.creditrama.teamd.clientservice.service;

import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.Client;

import java.util.List;

public interface ClientService {

    Client save(Client customer);

    Client fetchById(int profileId);

    Client fetchByName(String name);

    List<Client> fetchAllClient();

    void deleteById(int userId);
}