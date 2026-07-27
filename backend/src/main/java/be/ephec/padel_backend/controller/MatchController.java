package be.ephec.padel_backend.controller;

import be.ephec.padel_backend.dto.CreerMatchRequest;
import be.ephec.padel_backend.entity.Match;
import be.ephec.padel_backend.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final ReservationService reservationService;

    @Autowired
    public MatchController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<?> creerMatch(@RequestBody CreerMatchRequest request) {
        try {
            Match.Statut statut = Match.Statut.valueOf(request.getStatut());
            Match match = reservationService.creerMatch(
                    request.getIdTerrain(),
                    request.getDateHeureDebut(),
                    statut,
                    request.getMatriculeOrganisateur()
            );
            return ResponseEntity.status(201).body(match);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Statut invalide (PRIVE ou PUBLIC attendu)");
        }
    }
}