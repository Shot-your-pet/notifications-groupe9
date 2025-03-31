package fr.miage.syp.notificationsgroupe9.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.miage.syp.notificationsgroupe9.model.entity.ProfileDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RabbitEventSender {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;


    public RabbitEventSender(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public record DemandeInfosUtilisateurs(List<UUID> idsKeycloak) implements Serializable { }

    public List<ProfileDTO> getUtilisateursFromUtilisateursService() {
        try {
            List<ProfileDTO> utilisateurs = rabbitTemplate.convertSendAndReceiveAsType("utilisateurs.liste_profile_utilisateurs", new DemandeInfosUtilisateurs(List.of(UUID.randomUUID())), new ParameterizedTypeReference<List<ProfileDTO>>() {});
            return utilisateurs;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public record Message<T>(UUID idDemande, UUID idReponse, T data) implements Serializable {}
    public record MessageNewAvatar(UUID idKeycloak, Long idImage) implements Serializable {}

    public <T> void send(UUID idDemande, UUID idReponse, T data){
        Message message = new Message(idDemande, idReponse, data);
        this.rabbitTemplate.convertAndSend("exchange", "routingkey", message);
    }

    public void sendUpdateAvatarEvent(UUID idKeycloak, long idImage) {
        MessageNewAvatar message = new MessageNewAvatar(idKeycloak, idImage);
        this.rabbitTemplate.convertAndSend("images.update_avatar", message);
    }
}
