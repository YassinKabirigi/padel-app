package be.ephec.padel_backend.service;

import be.ephec.padel_backend.entity.Terrain;
import be.ephec.padel_backend.repository.TerrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TerrainService {

    private final TerrainRepository terrainRepository;

    @Autowired
    public TerrainService(TerrainRepository terrainRepository) {
        this.terrainRepository = terrainRepository;
    }

    public List<Terrain> getAllTerrains() {
        return terrainRepository.findAll();
    }

    public Optional<Terrain> getTerrainById(Integer id) {
        return terrainRepository.findById(id);
    }

    public List<Terrain> getTerrainsBySite(Integer idSite) {
        return terrainRepository.findBySiteIdSite(idSite);
    }

    public Terrain createTerrain(Terrain terrain) {
        return terrainRepository.save(terrain);
    }

    public void deleteTerrain(Integer id) {
        terrainRepository.deleteById(id);
    }
}