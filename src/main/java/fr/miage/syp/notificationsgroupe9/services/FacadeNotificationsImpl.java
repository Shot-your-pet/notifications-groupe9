package fr.miage.syp.notificationsgroupe9.services;


import fr.miage.syp.notificationsgroupe9.model.entity.ChallengeDuJourDTO;
import fr.miage.syp.notificationsgroupe9.model.entity.ProfileDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacadeNotificationsImpl implements FacadeNotifications {

    private final RabbitEventSender rabbitEventSender;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    String mailUsername;

    private static final Logger LOG = LoggerFactory.getLogger(FacadeNotificationsImpl.class);

    public FacadeNotificationsImpl(RabbitEventSender rabbitTemplate, JavaMailSender mailSender) {
        this.rabbitEventSender = rabbitTemplate;
        this.mailSender = mailSender;
    }


    @Override
    public List<ProfileDTO> getProfilesUtilisateurs() {
        return this.rabbitEventSender.getUtilisateursFromUtilisateursService();
    }

    @Override
    public void envoyerNotificationMail(List<ProfileDTO> utilisateurs, ChallengeDuJourDTO challenge) {
        for (ProfileDTO utilisateur : utilisateurs) {
            if (utilisateur.email() == null || utilisateur.email().isEmpty()) {
                LOG.warn("L'utilisateur {} n'a pas d'email, impossible de lui envoyer une notification", utilisateur.nom());
                continue;
            }
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(utilisateur.email());
            message.setFrom("Shot Your Pet <" + this.mailUsername + ">");
            message.setSubject("Nouveau challenge du jour !");
            message.setText(
                    "Bonjour : " + utilisateur.prenom() + " " + utilisateur.nom().toUpperCase() + ".\n" +
                    "Un nouveau challenge a été annoncé ! \n" +
                    "Le challenge du jour est : " + challenge.challenge().titre() + " - " + challenge.challenge().description() + "\n" +
                    "A très vite sur Shot Your Pet !"
            );
            this.mailSender.send(message);
            LOG.info("Email envoyé à {} pour le challenge du jour", utilisateur.email());
        }
    }


}
