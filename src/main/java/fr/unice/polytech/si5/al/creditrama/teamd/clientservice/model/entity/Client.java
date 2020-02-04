package fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.entity;


import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.Bank;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.entity.security.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Builder
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Client extends User {

    @ElementCollection
    private List<String> bankAccounts = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.ALL})
    private List<RecipientAccount> recipients;


    @ManyToOne(optional = false)
    private Bank bank;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;
}
