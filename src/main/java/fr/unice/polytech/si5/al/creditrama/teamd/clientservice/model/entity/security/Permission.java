package fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.entity.security;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "permission")
@Data
public class Permission implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer permissionId;
    @Column(name = "permission_name")
    private String permission_name;
}
