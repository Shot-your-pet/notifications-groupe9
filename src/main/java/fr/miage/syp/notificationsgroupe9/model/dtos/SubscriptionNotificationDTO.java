package fr.miage.syp.notificationsgroupe9.model.dtos;

import nl.martijndwars.webpush.Subscription;

public record SubscriptionNotificationDTO(
        Subscription subscription
) {
}