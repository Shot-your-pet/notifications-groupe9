package fr.miage.syp.notificationsgroupe9.model.dtos;

import java.io.Serializable;

public record ChallengeDTO(
        String titre,
        String description

) implements Serializable {
}
