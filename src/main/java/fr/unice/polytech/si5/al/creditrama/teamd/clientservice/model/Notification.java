package fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
@Data
@ToString
@NoArgsConstructor
public class Notification {
    private String type;
    private String action;
    private List<String> to;
    private List<NotificationMetaData> params;
}
