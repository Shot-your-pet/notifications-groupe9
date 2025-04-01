package fr.miage.syp.notificationsgroupe9.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class SubscriptionNotification {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000, unique = true)
    private String endpoint;

    private String authKey;

    private String p256dhKey;

    private LocalDateTime lastUpdate;

    private UUID idUtilisateur;

    public SubscriptionNotification() {
    }

    public SubscriptionNotification(String endpoint, String authKey, String p256dhKey, UUID utilisateur) {
        this.endpoint = endpoint;
        this.authKey = authKey;
        this.p256dhKey = p256dhKey;
        this.idUtilisateur = utilisateur;
        this.lastUpdate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getAuthKey() {
        return authKey;
    }

    public String getP256dhKey() {
        return p256dhKey;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public void setP256dhKey(String p256dhKey) {
        this.p256dhKey = p256dhKey;
    }


    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void updateLastUpdate(){
        this.lastUpdate = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "SubscriptionNotification{" +
                "id=" + id +
                ", endpoint='" + endpoint + '\'' +
                ", authKey='" + authKey + '\'' +
                ", p256dhKey='" + p256dhKey + '\'' +
                ", lastUpdate=" + lastUpdate +
                ", utilisateur=" + idUtilisateur +
                '}';
    }


    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public UUID getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(UUID idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }
}
