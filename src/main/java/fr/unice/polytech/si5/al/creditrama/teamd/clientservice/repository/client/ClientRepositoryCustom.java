package fr.unice.polytech.si5.al.creditrama.teamd.clientservice.repository.client;

import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.Client;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepositoryCustom {
    Optional<Client> findByIban(final String iban);
}
