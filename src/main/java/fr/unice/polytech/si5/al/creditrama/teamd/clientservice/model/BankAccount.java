package fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class BankAccount implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer bankAccountId;

    private Double balance;
}
