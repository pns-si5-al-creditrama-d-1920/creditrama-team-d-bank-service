package fr.unice.polytech.si5.al.creditrama.teamd.bankservice.repository;

import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.Bank;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends JpaRepository<Bank,Long> {

}
