package fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
public class RecipientAccount {


    @Id
    private String iban;

    private long client;

    private String firstName;
    private String lastName;

    @JsonIgnore
    @ManyToMany(mappedBy = "recipients")
    private List<Client> creditors;

}
