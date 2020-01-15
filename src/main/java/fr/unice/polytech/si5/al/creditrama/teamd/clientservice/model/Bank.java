package fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Bank {
    @Id
    private Long bankCode;

    @Column(nullable = false)
    @NotNull
    @Pattern(regexp = "[A-Z]{2,3}")
    private String countryCode;

    @Column(nullable = false)
    private String bankName;

    private String bic;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bank")
    @JsonIgnore
    private List<Client> clients;

}
