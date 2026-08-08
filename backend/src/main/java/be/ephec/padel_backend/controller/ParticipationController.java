package be.ephec.padel_backend.controller;

import be.ephec.padel_backend.dto.ParticipationDetailDto;
import be.ephec.padel_backend.entity.Participation;
import be.ephec.padel_backend.repository.ParticipationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/participations")
public class ParticipationController {

    private final ParticipationRepository participationRepository;

    @Autowired
    public ParticipationController(ParticipationRepository participationRepository) {
        this.participationRepository = participationRepository;
    }

    @GetMapping
    public List<ParticipationDetailDto> getAllParticipations() {
        List<Participation> participations = participationRepository.findAll();

        return participations.stream().map(p -> new ParticipationDetailDto(
                p.getIdParticipation(),
                p.getMembre().getMatricule(),
                p.getMembre().getPrenom() + " " + p.getMembre().getNom(),
                p.getMatch().getDateHeureDebut(),
                p.getMatch().getTerrain().getNumero(),
                p.getMatch().getTerrain().getSite().getNom(),
                p.getMatch().getStatut().name(),
                p.getEstOrganisateur(),
                p.getPaiement() != null,
                p.getPaiement() != null ? p.getPaiement().getMontant().toString() + " EUR" : "Non paye"
        )).toList();
    }
}