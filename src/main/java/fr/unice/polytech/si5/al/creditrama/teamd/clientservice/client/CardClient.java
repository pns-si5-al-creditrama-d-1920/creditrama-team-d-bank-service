package fr.unice.polytech.si5.al.creditrama.teamd.clientservice.client;

import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.BankAccountInformation;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.Card;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "card-client", url = "${service.card}", configuration = FeignConfiguration.class)
public interface CardClient {

    @PostMapping("/cards")
    Card createCard(@RequestBody BankAccountInformation bankAccountInformation);
}