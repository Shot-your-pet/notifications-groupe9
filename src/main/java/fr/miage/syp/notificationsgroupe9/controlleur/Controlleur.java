package fr.miage.syp.notificationsgroupe9.controlleur;

import fr.miage.syp.notificationsgroupe9.model.entity.ProfileDTO;
import fr.miage.syp.notificationsgroupe9.services.FacadeNotifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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



}
