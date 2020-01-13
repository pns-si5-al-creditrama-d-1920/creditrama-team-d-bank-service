package fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model;


import lombok.*;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class BankAccountRequest {
    private double amount;
}
