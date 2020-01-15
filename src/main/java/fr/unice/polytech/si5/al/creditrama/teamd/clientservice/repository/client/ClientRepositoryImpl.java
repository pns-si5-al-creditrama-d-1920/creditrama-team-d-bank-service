package fr.unice.polytech.si5.al.creditrama.teamd.clientservice.repository.client;

import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.Client;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class ClientRepositoryImpl implements ClientRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Optional<Client> findByIban(final String iban) {
        System.out.println("none");
        return Optional.empty();
    }
}