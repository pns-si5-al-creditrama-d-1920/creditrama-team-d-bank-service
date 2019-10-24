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

import java.util.List;
import java.util.Optional;

@Component
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
            throw new BankAccountNotFoundException(account + " doesn't exist");
        }
        return account.get();
    }

    private BankAccount getBankAccount(BankAccount bankAccount) throws BankAccountNotFoundException {
        Optional<BankAccount> account = bankAccountRepository.findById(bankAccount.getBankAccountId());
        if (!account.isPresent()) {
            throw new BankAccountNotFoundException(account + " doesn't exist");
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
        BankAccount newAccount = BankAccount.builder()
                .balance(account.getBankAccountId())
                .build();
        client.getBankAccounts().add(newAccount);
        clientRepository.save(client);
        return newAccount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendGreeting(Integer clientId) throws ClientNotFoundException {
        Client client = getClient(clientId);
        notificationService.sendGreeting("{\"email\": \"" + client.getEmail() + "\"}");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BankAccount> retrieveClientRecipients(Integer clientId) throws ClientNotFoundException {
        return getClient(clientId).getRecipients();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BankAccount createClientRecipient(Integer clientId, BankAccount recipientBankAccount) throws ClientNotFoundException, BankAccountNotFoundException {
        Client client = getClient(clientId);
        BankAccount account = getBankAccount(recipientBankAccount);
        account.setBalance(null);

        if (client.getRecipients().contains(account) || client.getBankAccounts().contains(account)) {
            return account;
        }

        client.getRecipients().add(account);
        clientRepository.save(client);
        return account;
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
        if (!client.getRecipients().contains(destinationAccount)) {
            throw new InvalidBankTransactionException("The receiver of the transaction is not is not is the sender recipients' list");
        }

        Integer previousBalanceSender = sourceAccount.getBalance();
        Integer previousBalanceReceiver = destinationAccount.getBalance();

        sourceAccount.setBalance(previousBalanceSender - transaction.getAmount());
        destinationAccount.setBalance(previousBalanceReceiver + transaction.getAmount());

        bankAccountRepository.save(sourceAccount);
        bankAccountRepository.save(destinationAccount);

        client.getTransactions().add(transaction);
        Client recipientClient = clientRepository.findByBankAccounts(destinationAccount);
        recipientClient.getTransactions().add(transaction);

        clientRepository.save(client);
        clientRepository.save(recipientClient);

        return transaction;
    }
}
