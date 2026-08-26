package be.ephec.padel_backend.service;

import be.ephec.padel_backend.dto.PenaliteDto;
import be.ephec.padel_backend.entity.Membre;
import be.ephec.padel_backend.repository.MembreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PenaliteService {

    private final MembreRepository membreRepository;

    @Autowired
    public PenaliteService(MembreRepository membreRepository) {
        this.membreRepository = membreRepository;
    }

    public List<PenaliteDto> getPenalitesActives() {
        LocalDate aujourdhui = LocalDate.now();
        return membreRepository.findAll().stream()
                .filter(m -> m.getDateFinPenalite() != null
                        && m.getDateFinPenalite().isAfter(aujourdhui))
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

    public void leverPenalite(String matricule) {
        Membre membre = membreRepository.findById(matricule)
                .orElseThrow(() -> new IllegalStateException("Membre introuvable"));
        membre.setDateDebutPenalite(null);
        membre.setDateFinPenalite(null);
        membre.setMotifPenalite(null);
        membreRepository.save(membre);
    }
}
