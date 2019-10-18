package fr.unice.polytech.si5.al.creditrama.teamd.bankservice.repository;

import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository  extends JpaRepository<Client,Integer> {
}