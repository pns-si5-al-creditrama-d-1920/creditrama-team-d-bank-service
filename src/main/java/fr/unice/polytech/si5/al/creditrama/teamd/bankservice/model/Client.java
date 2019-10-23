package fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model;

import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.security.Role;
import fr.unice.polytech.si5.al.creditrama.teamd.bankservice.model.security.User;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Client extends User {


    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
    private List<BankAccount> bankAccounts;
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
    private List<BankAccount> recipients;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
    private List <BankTransaction> transactions;

    public Client(Integer id, String username, String password, String email, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, List<Role> roles, List<BankAccount> bankAccounts, List<BankAccount> recipients) {
        super(id, username, password, email, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, roles);
        this.bankAccounts = bankAccounts;
        this.recipients = recipients;
    }
}
