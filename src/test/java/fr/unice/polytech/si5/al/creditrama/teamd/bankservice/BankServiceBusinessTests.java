package fr.unice.polytech.si5.al.creditrama.teamd.bankservice;

import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.business.BankBusiness;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.exception.BankAccountNotFoundException;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.exception.InvalidBankTransactionException;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.BankAccount;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.BankTransaction;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.Client;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.repository.BankAccountRepository;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.repository.ClientRepository;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.service.ClientService;
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
    private ClientService clientService;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    private Integer clientId;
    private Integer bankAccountId;

    @Before
    public void setUp() {
        Client client = new Client(null, "nathan", "password", "n@gmail.com", true, true, true, true, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        client = clientService.save(client);
        clientId = client.getUserId();
        bankAccountId = client.getBankAccounts().get(0).getBankAccountId();
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
    public void manageRecipients() throws Exception {
        BankAccount account = BankAccount.builder().balance(999.99).build();
        Integer expectedBankAccountId = bankAccountRepository.save(account).getBankAccountId();

        Client client = new Client(null, "théo", "password", "n@gmail.com", true, true, true, true, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        client.getBankAccounts().add(account);
        client = clientRepository.save(client);
        Integer clientId = client.getUserId();

        // Add a recipient
        Integer recipient = business.createClientRecipient(this.clientId, expectedBankAccountId);
        assertTrue(business.retrieveClientRecipients(this.clientId).contains(recipient));

        recipient = business.createClientRecipient(this.clientId, expectedBankAccountId);
        assertNull(recipient);

        recipient = business.createClientRecipient(clientId, expectedBankAccountId);
        assertNull(recipient);

        // Remove a recipient
        business.removeRecipient(this.clientId, expectedBankAccountId);
        assertFalse(business.retrieveClientRecipients(this.clientId).contains(recipient));
    }

    @Test
    public void transferToRecipient() throws Exception {
        BankAccount account = BankAccount.builder().balance(1000.0).build();
        Integer bankAccountId = bankAccountRepository.save(account).getBankAccountId();

        Client client = new Client(new Integer(0), "alexis", "password", "al@gmail.com", true, true, true, true, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        client.getBankAccounts().add(account);
        Integer clientId = clientService.save(client).getUserId();

        assertEquals(100.0, bankAccountRepository.findById(this.bankAccountId).get().getBalance(), 0.01);
        assertEquals(1000.0, bankAccountRepository.findById(bankAccountId).get().getBalance(), 0.01);

        business.createClientRecipient(clientId, this.bankAccountId);

        BankTransaction transaction = BankTransaction.builder()
                .sourceId(bankAccountId)
                .destinationId(this.bankAccountId)
                .amount(500.0)
                .build();

        business.createClientTransaction(clientId, transaction);

        assertEquals(500.0, bankAccountRepository.findById(bankAccountId).get().getBalance(), 0.01);
        assertEquals(600.0, bankAccountRepository.findById(this.bankAccountId).get().getBalance(), 0.01);

    }

    @Test(expected = InvalidBankTransactionException.class)
    public void transferToSameAccount() throws Exception {
        BankTransaction transaction = BankTransaction.builder()
                .sourceId(bankAccountId)
                .destinationId(this.bankAccountId)
                .amount(500.0)
                .build();
        business.createClientTransaction(clientId, transaction);
    }

    @Test(expected = InvalidBankTransactionException.class)
    public void invalidTransaction() throws Exception {
        BankAccount account = BankAccount.builder().balance(1000.0).build();
        Integer bankAccountId = bankAccountRepository.save(account).getBankAccountId();

        Client client = new Client(null, "alexis", "password", "al@gmail.com", true, true, true, true, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        client.getBankAccounts().add(account);
        Integer clientId = clientRepository.save(client).getUserId();

        BankTransaction transaction = BankTransaction.builder()
                .sourceId(this.bankAccountId)
                .destinationId(bankAccountId)
                .amount(500.0)
                .build();

        business.createClientTransaction(clientId, transaction);
    }

    @Test(expected = InvalidBankTransactionException.class)
    public void transferToNonRecipient() throws Exception {
        BankAccount account = BankAccount.builder().balance(1000.0).build();
        Integer bankAccountId = bankAccountRepository.save(account).getBankAccountId();

        Client client = new Client(null, "alexis", "password", "al@gmail.com", true, true, true, true, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        client.getBankAccounts().add(account);
        Integer clientId = clientRepository.save(client).getUserId();

        BankTransaction transaction = BankTransaction.builder()
                .sourceId(bankAccountId)
                .destinationId(this.bankAccountId)
                .amount(500.0)
                .build();

        business.createClientTransaction(clientId, transaction);
    }

    @Test(expected = BankAccountNotFoundException.class)
    public void removeRecipientNotPresent() throws Exception {
        BankAccount account = BankAccount.builder().balance(999.99).build();
        Integer bankAccountId = bankAccountRepository.save(account).getBankAccountId();

        business.removeRecipient(this.clientId, bankAccountId);
    }
}
