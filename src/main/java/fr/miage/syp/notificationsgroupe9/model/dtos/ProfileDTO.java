package fr.miage.syp.notificationsgroupe9.model.dtos;

public record ProfileDTO(
        String pseudo,
        String nom,
        String prenom,
        String email,
        Long idAvatar
) {

}
