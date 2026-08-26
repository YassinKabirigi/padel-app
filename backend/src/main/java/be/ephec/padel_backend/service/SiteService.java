package be.ephec.padel_backend.service;

import be.ephec.padel_backend.entity.Site;
import be.ephec.padel_backend.repository.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SiteService {

    private final SiteRepository siteRepository;

    @Autowired
    public SiteService(SiteRepository siteRepository) {
        this.siteRepository = siteRepository;
    }

    public List<Site> getAllSites() {
        return siteRepository.findAll();
    }

    public Optional<Site> getSiteById(Integer id) {
        return siteRepository.findById(id);
    }

    public Site createSite(Site site) {
        return siteRepository.save(site);
    }

    public Site updateSite(Integer id, Site siteDetails) {
        Site site = siteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Site introuvable avec id " + id));
        site.setNom(siteDetails.getNom());
        site.setAdresse(siteDetails.getAdresse());
        site.setHeureOuverture(siteDetails.getHeureOuverture());
        site.setHeureFermeture(siteDetails.getHeureFermeture());
        return siteRepository.save(site);
    }

    public void deleteSite(Integer id) {
        siteRepository.deleteById(id);
    }
}