package be.ephec.padel_backend.controller;

import be.ephec.padel_backend.entity.Terrain;
import be.ephec.padel_backend.service.TerrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/terrains")
public class TerrainController {

    private final TerrainService terrainService;

    @Autowired
    public TerrainController(TerrainService terrainService) {
        this.terrainService = terrainService;
    }

    @GetMapping
    public List<Terrain> getAllTerrains() {
        return terrainService.getAllTerrains();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Terrain> getTerrainById(@PathVariable Integer id) {
        return terrainService.getTerrainById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/site/{idSite}")
    public List<Terrain> getTerrainsBySite(@PathVariable Integer idSite) {
        return terrainService.getTerrainsBySite(idSite);
    }

    @PostMapping
    public ResponseEntity<Terrain> createTerrain(@RequestBody Terrain terrain) {
        Terrain created = terrainService.createTerrain(terrain);
        return ResponseEntity.status(201).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTerrain(@PathVariable Integer id) {
        terrainService.deleteTerrain(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Terrain> updateTerrain(@PathVariable Integer id, @RequestBody Terrain terrain) {
        try {
            Terrain updated = terrainService.updateTerrain(id, terrain);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}