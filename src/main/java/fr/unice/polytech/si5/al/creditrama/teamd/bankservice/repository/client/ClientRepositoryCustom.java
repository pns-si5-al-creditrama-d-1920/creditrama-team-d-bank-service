package fr.unice.polytech.si5.al.creditrama.teamd.bankservice.repository.client;

import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.Client;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepositoryCustom {
    Optional<Client> findByIban(final String iban);
}
