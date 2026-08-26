package be.ephec.padel_backend.service;

import be.ephec.padel_backend.dto.MonProfilDto;
import be.ephec.padel_backend.entity.Administrateur;
import be.ephec.padel_backend.entity.Membre;
import be.ephec.padel_backend.repository.AdministrateurRepository;
import be.ephec.padel_backend.repository.MembreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MembreService {

    private final MembreRepository membreRepository;
    private final AdministrateurRepository administrateurRepository;

    @Autowired
    public MembreService(MembreRepository membreRepository,
                        AdministrateurRepository administrateurRepository) {
        this.membreRepository = membreRepository;
        this.administrateurRepository = administrateurRepository;
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
    public MonProfilDto getMonProfil(String identifiant) {
        if (identifiant.startsWith("ADMIN-")) {
            Integer idAdmin = Integer.parseInt(identifiant.substring(6));
            Administrateur admin = administrateurRepository.findById(idAdmin)
                    .orElseThrow(() -> new IllegalStateException("Administrateur introuvable"));
            String siteNom = admin.getSite() != null ? admin.getSite().getNom() : null;
            return new MonProfilDto(identifiant, admin.getNom(), admin.getPrenom(),
                    admin.getEmail(), null, "ADMIN_" + admin.getTypeAdmin().name(), siteNom);
        }
        Membre membre = membreRepository.findById(identifiant)
                .orElseThrow(() -> new IllegalStateException("Membre introuvable"));
        String siteNom = membre.getSite() != null ? membre.getSite().getNom() : null;
        return new MonProfilDto(membre.getMatricule(), membre.getNom(), membre.getPrenom(),
                membre.getEmail(), membre.getTelephone(), membre.getTypeMembre().name(), siteNom);
    }

}
