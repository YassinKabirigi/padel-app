package be.ephec.padel_backend.controller;

import be.ephec.padel_backend.entity.Membre;
import be.ephec.padel_backend.service.MembreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("/api/membres")
public class MembreController {

    private final MembreService membreService;

    @Autowired
    public MembreController(MembreService membreService) {
        this.membreService = membreService;
    }

    @GetMapping
    public List<Membre> getAllMembres() {
        return membreService.getAllMembres();
    }

    @GetMapping("/{matricule}")
    public ResponseEntity<Membre> getMembreByMatricule(@PathVariable String matricule) {
        return membreService.getMembreByMatricule(matricule)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Membre> createMembre(@RequestBody Membre membre) {
        Membre created = membreService.createMembre(membre);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{matricule}")
    public ResponseEntity<Membre> updateMembre(@PathVariable String matricule, @RequestBody Membre membre) {
        try {
            Membre updated = membreService.updateMembre(matricule, membre);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{matricule}")
    public ResponseEntity<Void> deleteMembre(@PathVariable String matricule) {
        membreService.deleteMembre(matricule);
        return ResponseEntity.noContent().build();
    }
}