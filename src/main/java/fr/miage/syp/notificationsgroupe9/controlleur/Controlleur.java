package fr.miage.syp.notificationsgroupe9.controlleur;

import fr.miage.syp.notificationsgroupe9.model.dtos.ProfileDTO;
import fr.miage.syp.notificationsgroupe9.services.FacadeNotifications;
import nl.martijndwars.webpush.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/notifications")
public class Controlleur {

    private final FacadeNotifications facadeNotifications;
    private static final Logger LOG = LoggerFactory.getLogger(Controlleur.class);


    public Controlleur(FacadeNotifications facadeNotifications) {
        this.facadeNotifications = facadeNotifications;
    }

    public record ReponseAPI<T>(
            int code,
            String message,
            T contenu
    ){}

    @GetMapping("/check")
    public ResponseEntity<ReponseAPI<String>> check() {
        LOG.debug("check ok");
        return new ResponseEntity<>(new ReponseAPI<>(HttpStatus.OK.value(), "OK", "OK"), HttpStatus.OK);
    }

    @GetMapping("/utilisateurs")
    public ResponseEntity<ReponseAPI<List<ProfileDTO>>> getUtilisateurs() {
        List<ProfileDTO> profileDTOS = facadeNotifications.getProfilesUtilisateurs();
        return new ResponseEntity<>(new ReponseAPI<>(HttpStatus.OK.value(), "OK", profileDTOS), HttpStatus.OK);
    }

    @PostMapping("/subscribe")
    public ResponseEntity<ReponseAPI<Void>> subscribeNotification(@RequestBody Subscription subscription, Authentication authentication) {
        LOG.debug("Abonnement reçu pour : {}", authentication.getName());
        this.facadeNotifications.saveSubscription(subscription, UUID.fromString(authentication.getName()));
        return ResponseEntity.ok(new ReponseAPI<>(200, "Good notifications", null));
    }



}
