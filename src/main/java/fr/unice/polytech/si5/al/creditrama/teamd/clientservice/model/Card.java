package fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model;

import lombok.*;

import java.io.Serializable;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card implements Serializable {
    Long number;
}
