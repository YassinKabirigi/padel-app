package be.ephec.padel_backend.controller;

import be.ephec.padel_backend.dto.JourFermetureDto;
import be.ephec.padel_backend.service.JourFermetureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jours-fermeture")
public class JourFermetureController {

    private final JourFermetureService jourFermetureService;

    @Autowired
    public JourFermetureController(JourFermetureService jourFermetureService) {
        this.jourFermetureService = jourFermetureService;
    }

    @GetMapping
    public List<JourFermetureDto> getAllFermetures() {
        return jourFermetureService.getAllFermetures();
    }

    @PostMapping
    public ResponseEntity<JourFermetureDto> createFermeture(@RequestBody Map<String, Object> body) {
        LocalDate dateFermeture = LocalDate.parse((String) body.get("dateFermeture"));
        String motif = (String) body.get("motif");
        Integer idSite = body.get("idSite") != null ? ((Number) body.get("idSite")).intValue() : null;

        JourFermetureDto fermeture = jourFermetureService.createFermeture(
                new JourFermetureService.LocalDateWrapper(dateFermeture, motif, idSite)
        );
        return ResponseEntity.status(201).body(fermeture);
    }
    @PutMapping("/{id}")
    public ResponseEntity<JourFermetureDto> updateFermeture(@PathVariable Integer id, @RequestBody Map<String, Object> body) {
        LocalDate dateFermeture = LocalDate.parse((String) body.get("dateFermeture"));
        String motif = (String) body.get("motif");
        Integer idSite = body.get("idSite") != null ? ((Number) body.get("idSite")).intValue() : null;

        JourFermetureDto fermeture = jourFermetureService.updateFermeture(
                id, new JourFermetureService.LocalDateWrapper(dateFermeture, motif, idSite)
        );
        return ResponseEntity.ok(fermeture);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFermeture(@PathVariable Integer id) {
        jourFermetureService.deleteFermeture(id);
        return ResponseEntity.noContent().build();
    }
}