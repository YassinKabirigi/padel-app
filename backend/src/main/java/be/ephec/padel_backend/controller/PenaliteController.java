package be.ephec.padel_backend.controller;

import be.ephec.padel_backend.dto.PenaliteDto;
import be.ephec.padel_backend.service.PenaliteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/penalites")
public class PenaliteController {

    private final PenaliteService penaliteService;

    @Autowired
    public PenaliteController(PenaliteService penaliteService) {
        this.penaliteService = penaliteService;
    }

    @GetMapping
    public List<PenaliteDto> getPenalitesActives() {
        return penaliteService.getPenalitesActives();
    }

    @DeleteMapping("/{matricule}")
    public ResponseEntity<Void> leverPenalite(@PathVariable String matricule) {
        penaliteService.leverPenalite(matricule);
        return ResponseEntity.noContent().build();
    }
}
