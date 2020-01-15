package fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model;

import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.security.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    @ElementCollection
    private Set<String> recipients;


    @ManyToOne(optional = false)
    private Bank bank;


}
