package be.ephec.padel_backend.service;

import be.ephec.padel_backend.dto.JourFermetureDto;
import be.ephec.padel_backend.entity.JourFermeture;
import be.ephec.padel_backend.entity.Site;
import be.ephec.padel_backend.repository.JourFermetureRepository;
import be.ephec.padel_backend.repository.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JourFermetureService {

    private final JourFermetureRepository jourFermetureRepository;
    private final SiteRepository siteRepository;

    @Autowired
    public JourFermetureService(JourFermetureRepository jourFermetureRepository,
                                SiteRepository siteRepository) {
        this.jourFermetureRepository = jourFermetureRepository;
        this.siteRepository = siteRepository;
    }

    public List<JourFermetureDto> getAllFermetures() {
        return jourFermetureRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public JourFermetureDto createFermeture(LocalDateWrapper wrapper) {
        JourFermeture fermeture = new JourFermeture();
        fermeture.setDateFermeture(wrapper.dateFermeture());
        fermeture.setMotif(wrapper.motif());

        if (wrapper.idSite() != null) {
            Site site = siteRepository.findById(wrapper.idSite())
                    .orElseThrow(() -> new IllegalStateException("Site introuvable"));
            fermeture.setSite(site);
        }

        JourFermeture sauvegarde = jourFermetureRepository.save(fermeture);
        return toDto(sauvegarde);
    }

    public void deleteFermeture(Integer id) {
        jourFermetureRepository.deleteById(id);
    }

    private JourFermetureDto toDto(JourFermeture fermeture) {
        return new JourFermetureDto(
                fermeture.getIdFermeture(),
                fermeture.getDateFermeture(),
                fermeture.getMotif(),
                fermeture.getSite() != null ? fermeture.getSite().getIdSite() : null,
                fermeture.getSite() != null ? fermeture.getSite().getNom() : null
        );
    }
    public JourFermetureDto updateFermeture(Integer id, LocalDateWrapper wrapper) {
        JourFermeture fermeture = jourFermetureRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Jour de fermeture introuvable"));

        fermeture.setDateFermeture(wrapper.dateFermeture());
        fermeture.setMotif(wrapper.motif());

        if (wrapper.idSite() != null) {
            Site site = siteRepository.findById(wrapper.idSite())
                    .orElseThrow(() -> new IllegalStateException("Site introuvable"));
            fermeture.setSite(site);
        } else {
            fermeture.setSite(null);
        }

        JourFermeture sauvegarde = jourFermetureRepository.save(fermeture);
        return toDto(sauvegarde);
    }
    public record LocalDateWrapper(java.time.LocalDate dateFermeture, String motif, Integer idSite) {}
}