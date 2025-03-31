package fr.miage.syp.notificationsgroupe9.services;

import fr.miage.syp.notificationsgroupe9.model.entity.ChallengeDuJourDTO;
import fr.miage.syp.notificationsgroupe9.model.entity.ProfileDTO;

import java.util.List;

public interface FacadeNotifications {

    List<ProfileDTO> getProfilesUtilisateurs();

    void envoyerNotificationMail(List<ProfileDTO> utilisateurs, ChallengeDuJourDTO challenge);
}
