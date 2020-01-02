package fr.unice.polytech.si5.al.creditrama.teamd.bankservice;

import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.Bank;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.service.BankService;
import org.iban4j.BicFormatException;
import org.iban4j.BicUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@EnableResourceServer
@SpringBootApplication
@EnableJpaRepositories
public class BankServiceApplication implements CommandLineRunner {

    private final BankService bankService;

    @Autowired
    public BankServiceApplication(BankService bankService) {
        this.bankService = bankService;
    }

    public static void main(String[] args) {
        SpringApplication.run(BankServiceApplication.class, args);
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
            } catch (BicFormatException e){
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}
