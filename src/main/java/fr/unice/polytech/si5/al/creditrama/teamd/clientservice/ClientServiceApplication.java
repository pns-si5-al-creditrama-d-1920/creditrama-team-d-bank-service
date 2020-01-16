package fr.unice.polytech.si5.al.creditrama.teamd.clientservice;

import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.Bank;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.service.BankService;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.service.ClientService;
import org.iban4j.BicFormatException;
import org.iban4j.BicUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@EnableResourceServer
@SpringBootApplication
@EnableJpaRepositories
@EnableFeignClients
public class ClientServiceApplication implements CommandLineRunner {

    @Autowired
    private BankService bankService;

    @Autowired
    private ClientService clientService;

    public static void main(String[] args) {
        SpringApplication.run(ClientServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (!bankService.haveBank()) {
            String bic = "CREDFRPPXXX";
            try {
                BicUtil.validate(bic);
                bankService.createBank(Bank.builder().bankCode(22041L)
                        .countryCode("FR")
                        .bankName("CreditRama")
                        .bic(bic)
                        .build());
            } catch (BicFormatException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        clientService.initAdmin("TT829Alexis");
    }
}
