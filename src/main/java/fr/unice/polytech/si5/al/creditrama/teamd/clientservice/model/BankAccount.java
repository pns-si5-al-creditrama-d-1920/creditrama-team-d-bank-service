package fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model;

import lombok.*;

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

    private Set<Long> cards;

    public void addMoney(double amount) {
        this.balance += amount;
    }

    public void removeMoney(double amount) {
        this.balance -= amount;
    }
}
