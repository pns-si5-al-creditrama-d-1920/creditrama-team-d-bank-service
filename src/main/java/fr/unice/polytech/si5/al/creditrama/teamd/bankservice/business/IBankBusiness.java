package fr.unice.polytech.si5.al.creditrama.teamd.bankservice.business;

import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.exception.BankAccountNotFoundException;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.exception.ClientNotFoundException;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.exception.InvalidBankTransactionException;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.BankAccount;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.BankTransaction;

import java.util.List;

public interface IBankBusiness {

    /**
     * Get the bank accounts of a client
     *
     * @param clientId id of the client
     * @return List of bank accounts
     * @throws ClientNotFoundException if clientId is not found
     */
    List<BankAccount> retrieveClientBankAccounts(Integer clientId) throws ClientNotFoundException;

    /**
     * Create a new bank account for a client
     *
     * @param clientId id of the client
     * @param account  new account
     * @return new created bank account
     * @throws ClientNotFoundException if clientId is not found
     */
    BankAccount createClientBankAccount(Integer clientId, BankAccount account) throws ClientNotFoundException;

    /**
     * Send a greeting to the client
     *
     * @param clientId id of the client
     * @param message
     * @throws ClientNotFoundException if clientId is not found
     */
    void sendEmail(Integer clientId, String message) throws ClientNotFoundException;

    /**
     * Get the recipients' list of the client
     *
     * @param clientId id of the client
     * @return List of the recipients
     * @throws ClientNotFoundException if clientId is not found
     */
    List<Integer> retrieveClientRecipients(Integer clientId) throws ClientNotFoundException;

    /**
     * Create a recipient for a client
     *
     * @param clientId               id of the client
     * @param recipientBankAccountId id of the bank account
     * @return new recipient created
     * @throws ClientNotFoundException if clientId is not found
     */
    Integer createClientRecipient(Integer clientId, Integer recipientBankAccountId) throws ClientNotFoundException, BankAccountNotFoundException;

    /**
     * Create a transaction for a client
     *
     * @param clientId    id of the client
     * @param transaction transaction to create
     * @return new Transaction created
     * @throws ClientNotFoundException if clientId is not found
     */
    BankTransaction createClientTransaction(Integer clientId, BankTransaction transaction) throws ClientNotFoundException, BankAccountNotFoundException, InvalidBankTransactionException;

    Integer getAccount(Integer bankAccountId) throws BankAccountNotFoundException;
}
