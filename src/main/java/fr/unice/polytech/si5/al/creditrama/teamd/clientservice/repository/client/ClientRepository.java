package fr.unice.polytech.si5.al.creditrama.teamd.clientservice.repository.client;

import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.BankAccount;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer>, ClientRepositoryCustom {
    Client findByUsername(String name);

    Client findByBankAccounts(BankAccount bankAccount);


    boolean existsByUsername(String name);
}