package fr.unice.polytech.si5.al.creditrama.teamd.clientservice.service;

import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.exception.ClientNotFoundException;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.kafka.NotificationStreams;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.BankTransaction;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.Client;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.Notification;
import fr.unice.polytech.si5.al.creditrama.teamd.clientservice.model.NotificationMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.util.ArrayList;

@Service
@EnableBinding(NotificationStreams.class)
@Profile("!disable-kafka")
public class NotificationService {
    private final NotificationStreams notificationStreams;
    private final ClientService clientService;

    @Autowired
    public NotificationService(NotificationStreams notificationStreams, ClientService clientService) {
        this.notificationStreams = notificationStreams;
        this.clientService = clientService;
    }

    public void sendMail(Notification notification) {

        MessageChannel messageChannel = notificationStreams.outboundNotification();

        messageChannel.send(MessageBuilder
                .withPayload(notification)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }

    public void sendEmail(BankTransaction createdTransaction) throws ClientNotFoundException {
        Client dest = clientService.fetchByName(createdTransaction.getIbanDestination()); //TODO NOT WORKING
        Notification notification = new Notification();
        notification.setType("EMAIL");
        notification.setAction("TRANSFER");
        notification.setTo(new ArrayList<>());
        notification.getTo().add(dest.getEmail());
        notification.setParams(new ArrayList<>());
        notification.getParams().add(new NotificationMetaData("username", dest.getUsername()));
        notification.getParams().add(new NotificationMetaData("amount", createdTransaction.getAmount() + ""));
        sendMail(notification);
    }
}
