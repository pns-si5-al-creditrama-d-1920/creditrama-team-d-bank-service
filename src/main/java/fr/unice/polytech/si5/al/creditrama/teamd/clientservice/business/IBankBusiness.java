package fr.unice.polytech.si5.al.creditrama.teamd.clientservice.business;

import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.exception.BankAccountNotFoundException;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.exception.ClientNotFoundException;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.exception.InvalidBankTransactionException;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.BankAccount;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.BankTransaction;

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

    /**
     * Get the id of the client given a bank account id
     *
     * @param bankAccountId id of the bank account
     * @return id of the client
     * @throws BankAccountNotFoundException if bankAccountId is not found
     */
    Integer getAccount(Integer bankAccountId) throws BankAccountNotFoundException;

    /**
     * Remove a recipient from a client
     *
     * @param clientId    id of the client
     * @param recipientId id of the recipient (bank account)
     */
    void removeRecipient(Integer clientId, Integer recipientId) throws ClientNotFoundException, BankAccountNotFoundException;

    void sendEmail(BankTransaction createdTransaction) throws BankAccountNotFoundException;
}
