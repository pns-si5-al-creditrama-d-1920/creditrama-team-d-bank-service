package fr.unice.polytech.si5.al.creditrama.teamd.clientservice.repository;

import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount,Integer> {

}
