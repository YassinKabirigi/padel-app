package be.ephec.padel_backend.service;

import be.ephec.padel_backend.entity.Membre;
import be.ephec.padel_backend.repository.MembreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MembreService {

    private final MembreRepository membreRepository;

    @Autowired
    public MembreService(MembreRepository membreRepository) {
        this.membreRepository = membreRepository;
    }

    public List<Membre> getAllMembres() {
        return membreRepository.findAll();
    }

    public Optional<Membre> getMembreByMatricule(String matricule) {
        return membreRepository.findById(matricule);
    }

    public Membre createMembre(Membre membre) {
        return membreRepository.save(membre);
    }

    public Membre updateMembre(String matricule, Membre membreDetails) {
        Membre membre = membreRepository.findById(matricule)
                .orElseThrow(() -> new RuntimeException("Membre introuvable avec matricule " + matricule));
        membre.setNom(membreDetails.getNom());
        membre.setPrenom(membreDetails.getPrenom());
        membre.setEmail(membreDetails.getEmail());
        membre.setTelephone(membreDetails.getTelephone());
        return membreRepository.save(membre);
    }

    public void deleteMembre(String matricule) {
        membreRepository.deleteById(matricule);
    }
}