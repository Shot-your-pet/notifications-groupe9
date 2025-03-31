package fr.miage.syp.notificationsgroupe9.services;

import fr.miage.syp.notificationsgroupe9.model.entity.ChallengeDuJourDTO;
import fr.miage.syp.notificationsgroupe9.model.entity.ProfileDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RabbitEventListener {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitEventListener.class);
    private final FacadeNotifications facadeNotifications;

    public RabbitEventListener(FacadeNotifications facadeNotifications) {
        this.facadeNotifications = facadeNotifications;
    }

    @RabbitListener(queues = "challenges.nouveau_challenge_jour")
    public void receiveNewChallenge(ChallengeDuJourDTO challenge) {
        LOG.info("Nouveau challenge reçu : {}", challenge);
        List<ProfileDTO> utilisateurs = facadeNotifications.getProfilesUtilisateurs();
        facadeNotifications.envoyerNotificationMail(utilisateurs, challenge);
    }


}
