package be.ephec.padel_backend.controller;

import be.ephec.padel_backend.dto.ParticipationDetailDto;
import be.ephec.padel_backend.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/participations")
public class ParticipationController {

    private final ReservationService reservationService;

    @Autowired
    public ParticipationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<ParticipationDetailDto> getAllParticipations() {
        return reservationService.getAllParticipationDetails();
    }

    @GetMapping("/me")
    public List<ParticipationDetailDto> getMesParticipations(Authentication authentication) {
        String identifiant = authentication.getName();
        if (identifiant.startsWith("ADMIN-")) {
            return List.of();
        }
        return reservationService.getParticipationDetailsParMembre(identifiant);
    }
}
