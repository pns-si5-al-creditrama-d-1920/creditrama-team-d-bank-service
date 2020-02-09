package fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model;

import lombok.*;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class BankAccountInformation implements Serializable {
    String iban;
    String firstName;
    String lastName;
}
