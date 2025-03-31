package fr.miage.syp.notificationsgroupe9.model;

import java.io.Serializable;
import java.util.UUID;

public record UtilisateurDTO(
        UUID idUtilisateur,
        String pseudo,
        Long idAvatar
) implements Serializable {
}
