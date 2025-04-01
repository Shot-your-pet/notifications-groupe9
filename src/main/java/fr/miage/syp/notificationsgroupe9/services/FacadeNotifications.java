package fr.miage.syp.notificationsgroupe9.services;

import fr.miage.syp.notificationsgroupe9.model.dtos.ChallengeDuJourDTO;
import fr.miage.syp.notificationsgroupe9.model.dtos.ProfileDTO;
import nl.martijndwars.webpush.Subscription;
import org.jose4j.lang.JoseException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public interface FacadeNotifications {

    List<ProfileDTO> getProfilesUtilisateurs();

    void envoyerNotificationMail(List<ProfileDTO> utilisateurs, ChallengeDuJourDTO challenge);
    void envoyerNotificationsPush(List<ProfileDTO> utilisateurs, ChallengeDuJourDTO challenge) throws GeneralSecurityException, JoseException, IOException, ExecutionException, InterruptedException;

    void saveSubscription(Subscription subscription, UUID idUtilisateur);
}
