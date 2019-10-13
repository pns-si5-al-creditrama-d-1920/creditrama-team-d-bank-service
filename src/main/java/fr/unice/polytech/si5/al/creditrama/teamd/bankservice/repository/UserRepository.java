package fr.unice.polytech.si5.al.creditrama.teamd.bankservice.repository;

import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<Client, Integer> {

}
