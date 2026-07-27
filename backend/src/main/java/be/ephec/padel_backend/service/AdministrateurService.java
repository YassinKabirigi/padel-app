package be.ephec.padel_backend.service;

import be.ephec.padel_backend.entity.Administrateur;
import be.ephec.padel_backend.repository.AdministrateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdministrateurService {

    private final AdministrateurRepository administrateurRepository;

    @Autowired
    public AdministrateurService(AdministrateurRepository administrateurRepository) {
        this.administrateurRepository = administrateurRepository;
    }

    public List<Administrateur> getAllAdministrateurs() {
        return administrateurRepository.findAll();
    }

    public Optional<Administrateur> getAdministrateurById(Integer id) {
        return administrateurRepository.findById(id);
    }

    public Administrateur createAdministrateur(Administrateur administrateur) {
        return administrateurRepository.save(administrateur);
    }

    public Administrateur updateAdministrateur(Integer id, Administrateur details) {
        Administrateur administrateur = administrateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrateur introuvable avec id " + id));
        administrateur.setNom(details.getNom());
        administrateur.setPrenom(details.getPrenom());
        administrateur.setEmail(details.getEmail());
        return administrateurRepository.save(administrateur);
    }

    public void deleteAdministrateur(Integer id) {
        administrateurRepository.deleteById(id);
    }
}