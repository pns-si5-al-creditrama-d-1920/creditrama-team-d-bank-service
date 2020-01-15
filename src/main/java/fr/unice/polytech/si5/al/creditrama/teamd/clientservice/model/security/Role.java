package fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.security;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "role")
@Data
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer roleId;
    @Column(name = "role_name")
    private String role_name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "permission_role", joinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "roleId")}, inverseJoinColumns = {
            @JoinColumn(name = "permission_id", referencedColumnName = "permissionId")})
    private List<Permission> permissions;
}
