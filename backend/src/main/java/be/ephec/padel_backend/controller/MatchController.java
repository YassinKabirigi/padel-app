package be.ephec.padel_backend.controller;

import be.ephec.padel_backend.dto.CreerMatchRequest;
import be.ephec.padel_backend.dto.MatchDisponibleDto;
import be.ephec.padel_backend.entity.Match;
import be.ephec.padel_backend.entity.Participation;
import be.ephec.padel_backend.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final ReservationService reservationService;

    @Autowired
    public MatchController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<Match> creerMatch(@RequestBody CreerMatchRequest request,
                                            Authentication authentication) {
        Match.Statut statut = Match.Statut.valueOf(request.getStatut());
        LocalDateTime dateHeureDebut = LocalDateTime.parse(request.getDateHeureDebut());

        // L'organisateur est toujours le membre connecté — pas de champ dans la requête
        String matriculeOrganisateur = authentication.getName();

        Match match = reservationService.creerMatch(
                request.getIdTerrain(),
                dateHeureDebut,
                statut,
                matriculeOrganisateur,
                request.getMatriculesCoequipiers()
        );
        return ResponseEntity.status(201).body(match);
    }

    @GetMapping("/disponibles")
    public List<MatchDisponibleDto> getMatchsDisponibles(Authentication authentication) {
        String identifiant = authentication.getName();
        return reservationService.getMatchsDisponibles(identifiant);
    }

    @PostMapping("/{id}/rejoindre")
    public ResponseEntity<?> rejoindreMatch(@PathVariable Integer id, Authentication authentication) {
        String matricule = authentication.getName();
        Participation participation = reservationService.rejoindreMatch(id, matricule);
        return ResponseEntity.status(201).body(participation);
    }
}
