package fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model;

import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.security.Role;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.security.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
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

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
    private List<BankAccount> bankAccounts = new ArrayList<>();
    @ElementCollection
    private List<Integer> recipients;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE})
    private List <BankTransaction> bankTransactions;

    @ManyToOne(optional = false)
    private Bank bank;

    @Column(nullable = false, unique = true)
    private String iban;

    public Client(Integer id,
                  String username,
                  String password,
                  String email,
                  boolean enabled,
                  boolean accountNonExpired,
                  boolean credentialsNonExpired,
                  boolean accountNonLocked,
                  List<Role> roles,
                  List<BankAccount> bankAccounts,
                  List<Integer> recipients,
                  List<BankTransaction> bankTransactions) {
        super(id, username, password, email, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, roles);
        this.bankAccounts = bankAccounts;
        this.recipients = recipients;
        this.bankTransactions = bankTransactions;
    }
}
