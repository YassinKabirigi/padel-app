package be.ephec.padel_backend.controller;

import be.ephec.padel_backend.dto.MesPaiementsDto;
import be.ephec.padel_backend.dto.MesStatistiquesDto;
import be.ephec.padel_backend.dto.MonProfilDto;
import be.ephec.padel_backend.entity.Administrateur;
import be.ephec.padel_backend.entity.Membre;
import be.ephec.padel_backend.entity.Participation;
import be.ephec.padel_backend.repository.AdministrateurRepository;
import be.ephec.padel_backend.repository.MembreRepository;
import be.ephec.padel_backend.repository.ParticipationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/membres/me")
public class MembreMeController {

    private static final BigDecimal PRIX_PAR_JOUEUR = new BigDecimal("15.00");

    private final MembreRepository membreRepository;
    private final AdministrateurRepository administrateurRepository;
    private final ParticipationRepository participationRepository;

    @Autowired
    public MembreMeController(MembreRepository membreRepository,
                              AdministrateurRepository administrateurRepository,
                              ParticipationRepository participationRepository) {
        this.membreRepository = membreRepository;
        this.administrateurRepository = administrateurRepository;
        this.participationRepository = participationRepository;
    }

    private boolean isAdministrateur(String identifiant) {
        return identifiant.startsWith("ADMIN-");
    }

    @GetMapping
    public ResponseEntity<MonProfilDto> getMonProfil(Authentication authentication) {
        String identifiant = authentication.getName();

        if (isAdministrateur(identifiant)) {
            Integer idAdmin = Integer.parseInt(identifiant.substring(6));
            Administrateur admin = administrateurRepository.findById(idAdmin)
                    .orElseThrow(() -> new IllegalStateException("Administrateur introuvable"));

            String siteNom = admin.getSite() != null ? admin.getSite().getNom() : null;

            return ResponseEntity.ok(new MonProfilDto(
                    identifiant,
                    admin.getNom(),
                    admin.getPrenom(),
                    admin.getEmail(),
                    null,
                    "ADMIN_" + admin.getTypeAdmin().name(),
                    siteNom
            ));
        }

        Membre membre = membreRepository.findById(identifiant)
                .orElseThrow(() -> new IllegalStateException("Profil membre introuvable pour cet utilisateur"));
        String siteNom = membre.getSite() != null ? membre.getSite().getNom() : null;

        return ResponseEntity.ok(new MonProfilDto(
                membre.getMatricule(),
                membre.getNom(),
                membre.getPrenom(),
                membre.getEmail(),
                membre.getTelephone(),
                membre.getTypeMembre().name(),
                siteNom
        ));
    }

    @GetMapping("/paiements")
    public ResponseEntity<MesPaiementsDto> getMesPaiements(Authentication authentication) {
        String identifiant = authentication.getName();

        if (isAdministrateur(identifiant)) {
            return ResponseEntity.ok(new MesPaiementsDto(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
        }

        List<Participation> mesParticipations = participationRepository.findAll().stream()
                .filter(p -> p.getMembre().getMatricule().equals(identifiant))
                .toList();

        BigDecimal montantDu = PRIX_PAR_JOUEUR.multiply(BigDecimal.valueOf(mesParticipations.size()));
        BigDecimal montantPaye = mesParticipations.stream()
                .filter(p -> p.getPaiement() != null)
                .map(p -> PRIX_PAR_JOUEUR)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal soldeRestant = montantDu.subtract(montantPaye);

        return ResponseEntity.ok(new MesPaiementsDto(montantDu, montantPaye, soldeRestant));
    }

    @GetMapping("/stats")
    public ResponseEntity<MesStatistiquesDto> getMesStatistiques(Authentication authentication) {
        String identifiant = authentication.getName();

        if (isAdministrateur(identifiant)) {
            return ResponseEntity.ok(new MesStatistiquesDto(0, 0, 0, 0));
        }

        List<Participation> mesParticipations = participationRepository.findAll().stream()
                .filter(p -> p.getMembre().getMatricule().equals(identifiant))
                .toList();

        LocalDateTime maintenant = LocalDateTime.now();

        long matchsJoues = mesParticipations.stream()
                .filter(p -> p.getMatch().getDateHeureDebut().isBefore(maintenant))
                .count();
        long reservationsAVenir = mesParticipations.stream()
                .filter(p -> p.getMatch().getDateHeureDebut().isAfter(maintenant))
                .count();
        long matchsPrives = mesParticipations.stream()
                .filter(p -> p.getMatch().getStatut().name().equals("PRIVE"))
                .count();
        long matchsPublics = mesParticipations.stream()
                .filter(p -> p.getMatch().getStatut().name().equals("PUBLIC"))
                .count();

        return ResponseEntity.ok(new MesStatistiquesDto(matchsJoues, reservationsAVenir, matchsPrives, matchsPublics));
    }
}