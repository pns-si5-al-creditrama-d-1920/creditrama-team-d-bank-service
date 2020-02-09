package fr.unice.polytech.si5.al.creditrama.teamd.clientservice;

import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.client.BankAccountClient;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.client.CardClient;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.exception.BankAccountNotFoundException;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.exception.ClientNotFoundException;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.BankAccount;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.Card;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.entity.Client;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.entity.RecipientAccount;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.security.User;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.repository.client.ClientRepository;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.service.BankService;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(initializers = PropertyOverrideContextInitializer.class)
@ActiveProfiles("disable-kafka")
@Transactional
public class ClientServiceTests {
    @MockBean
    private BankAccountClient bankAccountClient;

    @MockBean
    private CardClient cardClient;

    @Autowired
    private BankService bankService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientService clientService;

    private User client;

    @BeforeEach
    public void setUp() {
        client = Client.builder().firstName("Nathan").lastName("Foray").bankAccounts(new ArrayList<>()).recipients(new ArrayList<>()).build();
        client.setUsername("nathan");
        client.setEmail("nathan@email.com");
        client.setPassword("password");
        client.setUserId(2L);
    }

    @Test
    void createClient() throws ClientNotFoundException {
        BankAccount expectedAccount = BankAccount.builder()
                .balance(50.0)
                .iban("FR8750")
                .bankCode(String.valueOf(bankService.getCurrentBank().get().getBankCode()))
                .build();
        Card expectedCard = Card.builder().number(42L).build();
        when(bankAccountClient.createAccount(anyLong(), any())).thenReturn(expectedAccount);
        when(cardClient.createCard(any())).thenReturn(expectedCard);

        Client expectedClient = clientService.save((Client) client);

        assertEquals(expectedClient, clientService.fetchById(expectedClient.getUserId()));
        assertEquals(expectedClient, clientService.fetchByName(expectedClient.getUsername()));
        assertTrue(clientService.fetchAllClient().contains(expectedClient));

        assertTrue(expectedClient.getBankAccounts().contains(expectedAccount.getIban()));

        clientService.deleteById(expectedClient.getUserId());

        assertThrows(ClientNotFoundException.class, () -> clientService.fetchById(client.getUserId()));
        assertNull(clientService.fetchByName(expectedClient.getUsername()));
        assertFalse(clientService.fetchAllClient().contains(expectedClient));
    }

    @Test
    void createBankAccount() throws ClientNotFoundException {
        BankAccount expectedAccount = BankAccount.builder()
                .balance(50.0)
                .iban("FR8750")
                .bankCode(String.valueOf(bankService.getCurrentBank().get().getBankCode()))
                .build();
        Card expectedCard = Card.builder().number(42L).build();
        when(bankAccountClient.createAccount(anyLong(), any())).thenReturn(expectedAccount);
        when(cardClient.createCard(any())).thenReturn(expectedCard);

        assertThrows(ClientNotFoundException.class, () -> clientService.createAccount(client.getUserId()));

        Client currentClient = clientService.save((Client) client);
        clientService.createAccount(currentClient.getUserId());

        currentClient = clientService.fetchById(currentClient.getUserId());
        assertTrue(currentClient.getBankAccounts().contains(expectedAccount.getIban()));

    }

    @Test
    void manageRecipients() throws ClientNotFoundException, BankAccountNotFoundException {
        BankAccount expectedAccount = BankAccount.builder()
                .balance(50.0)
                .iban("FR8750")
                .bankCode(String.valueOf(bankService.getCurrentBank().get().getBankCode()))
                .build();
        Card expectedCard = Card.builder().number(42L).build();
        when(bankAccountClient.createAccount(anyLong(), any())).thenReturn(expectedAccount);
        when(cardClient.createCard(any())).thenReturn(expectedCard);

        Client currentClient = clientService.save((Client) client);
        clientService.createAccount(currentClient.getUserId());

        expectedAccount.setClient(currentClient.getUserId());
        when(bankAccountClient.getBankAccount(anyString())).thenReturn(expectedAccount);

        List<String> ibans = currentClient.getRecipients().stream().map(RecipientAccount::getIban).collect(Collectors.toList());
        assertFalse(ibans.contains(expectedAccount.getIban()));

        BankAccount account = clientService.addRecipient(currentClient.getUserId(), "FR8750");

        currentClient = clientService.fetchById(currentClient.getUserId());
        ibans = currentClient.getRecipients().stream().map(RecipientAccount::getIban).collect(Collectors.toList());
        assertTrue(ibans.contains(account.getIban()));

        clientService.removeRecipient(currentClient.getUserId(), account.getIban());

        currentClient = clientService.fetchById(currentClient.getUserId());
        ibans = currentClient.getRecipients().stream().map(RecipientAccount::getIban).collect(Collectors.toList());
        assertFalse(ibans.contains(account.getIban()));
    }
}
