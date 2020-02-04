package fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.entity.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "oauth_client_details")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OauthClientDetails {
    @Id
    private String client_id;

    private String resource_ids;

    private String client_secret;

    private String scope;

    private String web_server_redirect_uri;

    private String authorities;
    private String authorized_grant_types;

    private int access_token_validity;

    private int refresh_token_validity;

    private String additional_information;

    private String autoapprove;
}
