package fr.unice.polytech.si5.al.creditrama.teamd.clientservice.repository.client;

import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.BankAccount;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByUsername(String name);



    boolean existsByUsername(String name);
}