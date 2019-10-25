package fr.unice.polytech.si5.al.creditrama.teamd.bankservice;

import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.business.BankBusiness;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.BankAccount;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.Client;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.repository.BankAccountRepository;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.repository.ClientRepository;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.service.NotificationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("disable-kafka")
@Transactional
public class BankServiceBusinessTests {

    @Autowired
    private BankBusiness business;

    @MockBean
    private NotificationService service;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    private Integer clientId;
    private Integer bankAccountId;

    @Before
    public void setUp() {
        clientId = 1;
        BankAccount account = BankAccount.builder().balance(999.99).build();
        bankAccountId = bankAccountRepository.save(account).getBankAccountId();

        Client client = new Client(clientId, "nathan", "password", "n@gmail.com", true, true, true, true, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        client.getBankAccounts().add(account);
        clientId = clientRepository.save(client).getUserId();
    }

    @Test
    public void manageBankAccountsOfClient() throws Exception {
        BankAccount expected = BankAccount.builder().balance(500.0).build();
        BankAccount received = business.createClientBankAccount(clientId, expected);
        Integer id = received.getBankAccountId();

        expected.setBankAccountId(id);
        Optional<BankAccount> actual = bankAccountRepository.findById(id);
        assertEquals(Optional.of(expected), actual);

        Optional<Client> client = clientRepository.findById(clientId);

        assertTrue(client.get().getBankAccounts().contains(expected));
        assertEquals(business.retrieveClientBankAccounts(clientId), client.get().getBankAccounts());
    }

    @Test
    public void recipients() throws Exception {
        Integer clientId = 140;
        BankAccount account = BankAccount.builder().balance(999.99).build();
        Integer bankAccountId = bankAccountRepository.save(account).getBankAccountId();

        Client client = new Client(clientId, "théo", "password", "n@gmail.com", true, true, true, true, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        client.getBankAccounts().add(account);
        client = clientRepository.save(client);
        clientId = client.getUserId();

        // Add a recipient
        BankAccount expected = BankAccount.builder().bankAccountId(bankAccountId).balance(192000000.00).build();

        BankAccount recipient = business.createClientRecipient(this.clientId, expected);
        assertNull(recipient.getBalance());
        assertTrue(business.retrieveClientRecipients(this.clientId).contains(recipient));

        recipient = business.createClientRecipient(this.clientId, expected);
        assertNull(recipient);

        recipient = business.createClientRecipient(clientId, expected);
        assertNull(recipient);
    }
}
