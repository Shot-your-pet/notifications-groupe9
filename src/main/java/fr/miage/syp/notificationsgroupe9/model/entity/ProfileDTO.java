package fr.miage.syp.notificationsgroupe9.model.entity;

public record ProfileDTO(
        String pseudo,
        String nom,
        String prenom,
        String email,
        Long idAvatar
) {

}
