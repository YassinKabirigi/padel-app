package be.ephec.padel_backend.service;

import be.ephec.padel_backend.entity.*;
        import be.ephec.padel_backend.repository.JourFermetureRepository;
import be.ephec.padel_backend.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReservationService {

    private static final int DUREE_MATCH_MINUTES = 90;
    private static final int BATTEMENT_MINUTES = 15;
    private static final int BLOCAGE_TOTAL_MINUTES = DUREE_MATCH_MINUTES + BATTEMENT_MINUTES; // 105

    private final MatchRepository matchRepository;
    private final JourFermetureRepository jourFermetureRepository;

    @Autowired
    public ReservationService(MatchRepository matchRepository,
                              JourFermetureRepository jourFermetureRepository) {
        this.matchRepository = matchRepository;
        this.jourFermetureRepository = jourFermetureRepository;
    }

    /**
     * Vérifie si un créneau est disponible pour un terrain donné,
     * en tenant compte des horaires du site, des fermetures et des
     * matches déjà existants (avec battement de 15 min).
     */
    public boolean isCreneauDisponible(Terrain terrain, LocalDateTime dateHeureDebut) {
        Site site = terrain.getSite();
        LocalDateTime dateHeureFin = dateHeureDebut.plusMinutes(DUREE_MATCH_MINUTES);

        if (!isSiteOuvert(site, dateHeureDebut.toLocalDate())) {
            return false;
        }

        if (!isDansHorairesSite(site, dateHeureDebut, dateHeureFin)) {
            return false;
        }

        if (isChevauchementAvecMatchExistant(terrain, dateHeureDebut)) {
            return false;
        }

        return true;
    }

    /**
     * Le site est ouvert s'il n'y a ni fermeture globale, ni fermeture
     * spécifique à ce site, à la date demandée.
     */
    private boolean isSiteOuvert(Site site, LocalDate date) {
        List<JourFermeture> fermetures = jourFermetureRepository.findAll();

        for (JourFermeture fermeture : fermetures) {
            boolean memeDate = fermeture.getDateFermeture().equals(date);
            boolean fermetureGlobale = fermeture.getSite() == null;
            boolean fermetureCeSite = fermeture.getSite() != null
                    && fermeture.getSite().getIdSite().equals(site.getIdSite());

            if (memeDate && (fermetureGlobale || fermetureCeSite)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Vérifie que le match (début et fin) reste dans les horaires
     * d'ouverture/fermeture du site.
     */
    private boolean isDansHorairesSite(Site site, LocalDateTime debut, LocalDateTime fin) {
        LocalDateTime ouvertureCeJour = debut.toLocalDate().atTime(site.getHeureOuverture());
        LocalDateTime fermetureCeJour = debut.toLocalDate().atTime(site.getHeureFermeture());

        return !debut.isBefore(ouvertureCeJour) && !fin.isAfter(fermetureCeJour);
    }

    /**
     * Vérifie qu'aucun match existant sur ce terrain ne chevauche le
     * nouveau créneau, en tenant compte du battement de 15 min de
     * chaque côté (donc un blocage réel de 105 min par match existant).
     */
    private boolean isChevauchementAvecMatchExistant(Terrain terrain, LocalDateTime nouveauDebut) {
        LocalDateTime nouveauFinBloquee = nouveauDebut.plusMinutes(BLOCAGE_TOTAL_MINUTES);

        List<Match> matchesExistants = matchRepository.findAll().stream()
                .filter(m -> m.getTerrain().getIdTerrain().equals(terrain.getIdTerrain()))
                .toList();

        for (Match existant : matchesExistants) {
            LocalDateTime existantDebut = existant.getDateHeureDebut();
            LocalDateTime existantFinBloquee = existantDebut.plusMinutes(BLOCAGE_TOTAL_MINUTES);

            // Chevauchement si les deux plages [debut, finBloquee] se croisent
            boolean chevauche = nouveauDebut.isBefore(existantFinBloquee)
                    && existantDebut.isBefore(nouveauFinBloquee);

            if (chevauche) {
                return true;
            }
        }
        return false;
    }

    /**
     * Vérifie que le membre respecte le délai minimum de réservation
     * selon son type (Global : 21j, Site : 14j, Libre : 5j).
     */
    public boolean isDelaiRespecte(Membre membre, LocalDateTime dateMatch) {
        long joursAvantMatch = ChronoUnit.DAYS.between(LocalDate.now(), dateMatch.toLocalDate());
        int delaiMinimum = getDelaiMinimumJours(membre.getTypeMembre());
        return joursAvantMatch >= delaiMinimum;
    }

    private int getDelaiMinimumJours(Membre.TypeMembre type) {
        return switch (type) {
            case GLOBAL -> 21;
            case SITE -> 14;
            case LIBRE -> 5;
        };
    }

    /**
     * Pour un MembreSite, vérifie que le terrain appartient bien
     * à son site de rattachement.
     */
    public boolean isTerrainAutorise(Membre membre, Terrain terrain) {
        if (membre.getTypeMembre() != Membre.TypeMembre.SITE) {
            return true; // Global et Libre peuvent réserver n'importe où
        }
        return membre.getSite() != null
                && membre.getSite().getIdSite().equals(terrain.getSite().getIdSite());
    }

    /**
     * Vérifie que le membre n'a pas de pénalité active à la date du jour.
     */
    public boolean isPenaliteActive(Membre membre) {
        if (membre.getDateFinPenalite() == null) {
            return false;
        }
        return membre.getDateFinPenalite().isAfter(LocalDate.now());
    }
}