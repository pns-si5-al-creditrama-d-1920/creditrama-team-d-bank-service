package fr.unice.polytech.si5.al.creditrama.teamd.bankservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BankServiceDatabaseConnectionTests {

    @Test
    public void testEmbeddedDatabase() {
       /** Client client = Client.builder().name("roger").build();
        repository.save(client);

        assertTrue(repository.findByName("roger").isPresent());**/
    }
}
