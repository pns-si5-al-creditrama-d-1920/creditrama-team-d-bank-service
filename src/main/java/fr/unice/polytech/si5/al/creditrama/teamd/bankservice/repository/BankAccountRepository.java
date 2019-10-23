package fr.unice.polytech.si5.al.creditrama.teamd.bankservice.repository;

import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount,Integer> {
}
