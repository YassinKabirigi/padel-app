package be.ephec.padel_backend.controller;

import be.ephec.padel_backend.dto.PenaliteDto;
import be.ephec.padel_backend.entity.Membre;
import be.ephec.padel_backend.repository.MembreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/penalites")
public class PenaliteController {

    private final MembreRepository membreRepository;

    @Autowired
    public PenaliteController(MembreRepository membreRepository) {
        this.membreRepository = membreRepository;
    }

    @GetMapping
    public List<PenaliteDto> getPenalitesActives() {
        LocalDate aujourdhui = LocalDate.now();

        return membreRepository.findAll().stream()
                .filter(m -> m.getDateFinPenalite() != null && m.getDateFinPenalite().isAfter(aujourdhui))
                .map(m -> new PenaliteDto(
                        m.getMatricule(),
                        m.getNom(),
                        m.getPrenom(),
                        m.getDateDebutPenalite(),
                        m.getDateFinPenalite(),
                        m.getMotifPenalite()
                ))
                .toList();
    }

    @DeleteMapping("/{matricule}")
    public ResponseEntity<Void> leverPenalite(@PathVariable String matricule) {
        Membre membre = membreRepository.findById(matricule)
                .orElseThrow(() -> new IllegalStateException("Membre introuvable"));

        membre.setDateDebutPenalite(null);
        membre.setDateFinPenalite(null);
        membre.setMotifPenalite(null);
        membreRepository.save(membre);

        return ResponseEntity.noContent().build();
    }
}