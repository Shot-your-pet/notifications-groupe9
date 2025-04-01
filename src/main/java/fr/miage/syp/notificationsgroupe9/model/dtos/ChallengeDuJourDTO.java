package fr.miage.syp.notificationsgroupe9.model.dtos;

import java.io.Serializable;
import java.time.Instant;

public record ChallengeDuJourDTO(
        ChallengeDTO challenge,
        Instant dateDebut,
        Instant dateFin
) implements Serializable {
}