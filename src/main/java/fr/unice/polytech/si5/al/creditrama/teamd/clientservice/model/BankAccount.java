package fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model;

import lombok.*;

import javax.persistence.ElementCollection;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class BankAccount {

    private Double balance;

    private String iban;

    private String bankCode;

    private long client;

    @ElementCollection
    private Set<String> creditors;

    public void addMoney(double amount) {
        this.balance += amount;
    }

    public void removeMoney(double amount) {
        this.balance -= amount;
    }
}
