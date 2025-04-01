package fr.miage.syp.notificationsgroupe9.model.repository;

import fr.miage.syp.notificationsgroupe9.model.entity.SubscriptionNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionNotificationRepository extends JpaRepository<SubscriptionNotification, Long> {
    Optional<SubscriptionNotification> findByEndpoint(String endpoint);
}
