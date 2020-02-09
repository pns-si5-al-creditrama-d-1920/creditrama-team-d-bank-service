package fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@NoArgsConstructor
public class Card implements Serializable {
    Long number;
}
