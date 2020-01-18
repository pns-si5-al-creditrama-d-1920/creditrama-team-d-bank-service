package fr.unice.polytech.si5.al.creditrama.teamd.clientservice.client;

import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.BankAccount;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.BankAccountRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "bankaccounts", url = "${service.bankaccount}", configuration = FeignConfiguration.class)
public interface BankAccountClient {

    @GetMapping("/accounts/{iban}")
    BankAccount getBankAccount(@PathVariable("iban") String iban);

    @PostMapping("/clients/{clientId}/accounts/")
    BankAccount createAccount(@PathVariable long clientId, @RequestBody BankAccountRequest bankAccountRequest);

    @PatchMapping("/accounts/")
    BankAccount updateBanAccount(@PathVariable String iban, @RequestParam double balance);

}