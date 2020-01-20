package fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMetaData {
    private String key;
    private String value;
}
