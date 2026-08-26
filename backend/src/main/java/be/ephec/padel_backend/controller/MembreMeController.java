package be.ephec.padel_backend.controller;

import be.ephec.padel_backend.dto.HistoriquePaiementDto;
import be.ephec.padel_backend.dto.MesPaiementsDto;
import be.ephec.padel_backend.dto.MesStatistiquesDto;
import be.ephec.padel_backend.dto.MonProfilDto;
import be.ephec.padel_backend.service.MembreService;
import be.ephec.padel_backend.service.PaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/membres/me")
public class MembreMeController {

    private final MembreService membreService;
    private final PaiementService paiementService;

    @Autowired
    public MembreMeController(MembreService membreService,
                              PaiementService paiementService) {
        this.membreService = membreService;
        this.paiementService = paiementService;
    }

    @GetMapping
    public ResponseEntity<MonProfilDto> getMonProfil(Authentication authentication) {
        return ResponseEntity.ok(membreService.getMonProfil(authentication.getName()));
    }

    @GetMapping("/paiements")
    public ResponseEntity<MesPaiementsDto> getMesPaiements(Authentication authentication) {
        String identifiant = authentication.getName();
        if (identifiant.startsWith("ADMIN-")) {
            return ResponseEntity.ok(new MesPaiementsDto(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
        }
        return ResponseEntity.ok(paiementService.getMesPaiements(identifiant));
    }

    @GetMapping("/stats")
    public ResponseEntity<MesStatistiquesDto> getMesStatistiques(Authentication authentication) {
        String identifiant = authentication.getName();
        if (identifiant.startsWith("ADMIN-")) {
            return ResponseEntity.ok(new MesStatistiquesDto(0, 0, 0, 0));
        }
        return ResponseEntity.ok(paiementService.getMesStatistiques(identifiant));
    }

    @GetMapping("/historique-paiements")
    public ResponseEntity<List<HistoriquePaiementDto>> getHistoriquePaiements(Authentication authentication) {
        String identifiant = authentication.getName();
        if (identifiant.startsWith("ADMIN-")) {
            return ResponseEntity.ok(List.of());
        }
        return ResponseEntity.ok(paiementService.getHistoriquePaiements(identifiant));
    }

    @PostMapping("/payer/{idParticipation}")
    public ResponseEntity<?> payerParticipation(@PathVariable Integer idParticipation,
                                                 Authentication authentication) {
        String identifiant = authentication.getName();
        if (identifiant.startsWith("ADMIN-")) {
            return ResponseEntity.status(403).body("Les administrateurs ne peuvent pas effectuer de paiement");
        }
        try {
            paiementService.payerParticipation(idParticipation, identifiant);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }
}
