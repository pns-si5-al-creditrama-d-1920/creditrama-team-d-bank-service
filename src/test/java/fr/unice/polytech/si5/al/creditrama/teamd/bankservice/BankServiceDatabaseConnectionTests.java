package fr.unice.polytech.si5.al.creditrama.teamd.bankservice;

import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.BankAccount;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.repository.BankAccountRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BankServiceDatabaseConnectionTests {

    @Autowired
    private BankAccountRepository repository;

    @Test
    public void testEmbeddedDatabase() {
        BankAccount account = BankAccount.builder().balance(100).build();
        repository.save(account);

        assertTrue(repository.findByBalance(100).isPresent());
    }
}
