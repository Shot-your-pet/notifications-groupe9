package fr.miage.syp.notificationsgroupe9.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.miage.syp.notificationsgroupe9.model.dtos.ChallengeDuJourDTO;
import fr.miage.syp.notificationsgroupe9.model.dtos.ProfileDTO;
import fr.miage.syp.notificationsgroupe9.model.entity.SubscriptionNotification;
import fr.miage.syp.notificationsgroupe9.model.repository.SubscriptionNotificationRepository;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;
import org.apache.http.HttpResponse;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class FacadeNotificationsImpl implements FacadeNotifications {

    private final ObjectMapper objectMapper;
    private String vapidPublicKey;
    private String vapidPrivateKey;

    private final RabbitEventSender rabbitEventSender;
    private final JavaMailSender mailSender;
    private final SubscriptionNotificationRepository subscriptionNotificationRepository;
    private final PushService pushService;

    @Value("${spring.mail.username}")
    String mailUsername;




    private static final Logger LOG = LoggerFactory.getLogger(FacadeNotificationsImpl.class);

    public FacadeNotificationsImpl(@Value("${vapid.private.key}") String vapidPrivateKey,
                                   @Value("${vapid.public.key}") String vapidPublicKey,
                                   RabbitEventSender rabbitTemplate, JavaMailSender mailSender, SubscriptionNotificationRepository subscriptionNotificationRepository, ObjectMapper objectMapper) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        this.vapidPrivateKey = vapidPrivateKey;
        this.vapidPublicKey = vapidPublicKey;
        this.rabbitEventSender = rabbitTemplate;
        this.mailSender = mailSender;
        this.subscriptionNotificationRepository = subscriptionNotificationRepository;
        this.pushService = new PushService();
        this.pushService.setPrivateKey(vapidPrivateKey);
        this.pushService.setPublicKey(vapidPublicKey);
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
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

    @Override
    public void saveSubscription(Subscription subscription, UUID idUtilisateur) {
        // Rechercher une souscription avec le même appareil, navigateur et OS pour l'utilisateur
        Optional<SubscriptionNotification> existingEndpoint = this.subscriptionNotificationRepository.findByEndpoint(subscription.endpoint);

        // Si un endpoint existe déjà pour un autre utilisateur, il faut l'associer au nouvel utilisateur
        if (existingEndpoint.isEmpty()) {
            SubscriptionNotification subscriptionNotification = new SubscriptionNotification(
                    subscription.endpoint,
                    subscription.keys.auth,
                    subscription.keys.p256dh,
                    idUtilisateur
            );
            this.subscriptionNotificationRepository.save(subscriptionNotification);
        }
    }

    @Override
    public void envoyerNotificationsPush(List<ProfileDTO> utilisateurs, ChallengeDuJourDTO challenge) throws GeneralSecurityException, JoseException, IOException, ExecutionException, InterruptedException {
        List<SubscriptionNotification> subscriptionNotifications = this.subscriptionNotificationRepository.findAll();
        for (SubscriptionNotification subscription : subscriptionNotifications) {

            Notification notification = new Notification(
                    subscription.getEndpoint(),
                    subscription.getP256dhKey(),
                    subscription.getAuthKey(),
                    this.objectMapper.writeValueAsString(challenge)
            );
            HttpResponse connexion = this.pushService.send(notification);
            LOG.debug("Notification envoyée à {} : {}", subscription.getEndpoint(), connexion.getStatusLine());
        }
        LOG.info("Notifications envoyées à {} utilisateurs", subscriptionNotifications.size());
    }
}
