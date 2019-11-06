package fr.unice.polytech.si5.al.creditrama.teamd.bankservice.business;

import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.exception.BankAccountNotFoundException;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.exception.ClientNotFoundException;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.exception.InvalidBankTransactionException;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.BankAccount;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.BankTransaction;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.Client;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.repository.BankAccountRepository;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.repository.ClientRepository;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class BankBusiness implements IBankBusiness {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private NotificationService notificationService;

    private Client getClient(Integer clientId) throws ClientNotFoundException {
        Optional<Client> client = clientRepository.findById(clientId);
        if (!client.isPresent()) {
            throw new ClientNotFoundException(clientId + " doesn't exist");
        }
        return client.get();
    }

    private BankAccount getBankAccount(Integer bankAccountId) throws BankAccountNotFoundException {
        Optional<BankAccount> account = bankAccountRepository.findById(bankAccountId);
        if (!account.isPresent()) {
            throw new BankAccountNotFoundException(bankAccountId + " doesn't exist");
        }
        return account.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BankAccount> retrieveClientBankAccounts(Integer clientId) throws ClientNotFoundException {
        return getClient(clientId).getBankAccounts();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BankAccount createClientBankAccount(Integer clientId, BankAccount account) throws ClientNotFoundException {
        Client client = getClient(clientId);
        BankAccount createdAccount = BankAccount.builder()
                .balance(account.getBalance())
                .build();
        bankAccountRepository.save(createdAccount);
        client.getBankAccounts().add(createdAccount);
        clientRepository.save(client);
        return createdAccount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendEmail(Integer clientId, String message) throws ClientNotFoundException {
        Client client = getClient(clientId);
        notificationService.sendGreeting("{\"email\": \"" + client.getEmail() + "\", \"message\": \"" + message + "\"}");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getAccount(Integer bankAccountId) throws BankAccountNotFoundException {
        List<Client> clients = clientRepository.findAll();
        for (Client client : clients) {
            for (BankAccount bankAccount : client.getBankAccounts()) {
                if (bankAccount.getBankAccountId().equals(bankAccountId)) {
                    return client.getUserId();
                }
            }
        }
        throw new BankAccountNotFoundException(bankAccountId + " doesn't exist");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> retrieveClientRecipients(Integer clientId) throws ClientNotFoundException {
        return getClient(clientId).getRecipients();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer createClientRecipient(Integer clientId, Integer recipientBankAccountId) throws ClientNotFoundException, BankAccountNotFoundException {
        Client client = getClient(clientId);
        BankAccount account = getBankAccount(recipientBankAccountId);

        if (client.getRecipients().contains(recipientBankAccountId) || client.getBankAccounts().contains(account)) {
            return null;
        }

        client.getRecipients().add(recipientBankAccountId);
        clientRepository.save(client);
        return recipientBankAccountId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeRecipient(Integer clientId, Integer recipientId) throws ClientNotFoundException, BankAccountNotFoundException {
        Client client = getClient(clientId);
        getBankAccount(recipientId);
        if (!client.getRecipients().contains(recipientId)) {
            throw new BankAccountNotFoundException("The client " + clientId + " doesn't have " + recipientId + " as a recipient.");
        }
        client.getRecipients().remove(recipientId);
        clientRepository.save(client);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BankTransaction createClientTransaction(Integer clientId, BankTransaction transaction) throws ClientNotFoundException, BankAccountNotFoundException, InvalidBankTransactionException {
        Client client = getClient(clientId);
        BankAccount sourceAccount = getBankAccount(transaction.getSourceId());
        BankAccount destinationAccount = getBankAccount(transaction.getDestinationId());

        if (!client.getBankAccounts().contains(sourceAccount)) {
            throw new InvalidBankTransactionException("The sender is not the source of the transaction");
        }
        if (!client.getRecipients().contains(transaction.getDestinationId())) {
            throw new InvalidBankTransactionException("The receiver of the transaction is not is not is the sender recipients' list");
        }

        Double previousBalanceSender = sourceAccount.getBalance();
        Double previousBalanceReceiver = destinationAccount.getBalance();

        sourceAccount.setBalance(previousBalanceSender - transaction.getAmount());
        destinationAccount.setBalance(previousBalanceReceiver + transaction.getAmount());

        bankAccountRepository.save(sourceAccount);
        bankAccountRepository.save(destinationAccount);

        client.getBankTransactions().add(transaction);
        Client recipientClient = clientRepository.findByBankAccounts(destinationAccount);
        recipientClient.getBankTransactions().add(transaction);

        clientRepository.save(client);
        clientRepository.save(recipientClient);

        return transaction;
    }
}
