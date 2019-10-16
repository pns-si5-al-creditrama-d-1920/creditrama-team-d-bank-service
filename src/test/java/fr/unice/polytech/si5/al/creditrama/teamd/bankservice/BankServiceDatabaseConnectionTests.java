package fr.unice.polytech.si5.al.creditrama.teamd.bankservice;

import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.Client;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.repository.ClientRepository;
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
    private ClientRepository repository;

    @Test
    public void testEmbeddedDatabase() {
        Client client = Client.builder().name("roger").build();
        repository.save(client);

        assertTrue(repository.findByName("roger").isPresent());
    }
}
