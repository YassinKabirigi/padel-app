package be.ephec.padel_backend.service;

import be.ephec.padel_backend.entity.*;
import be.ephec.padel_backend.repository.JourFermetureRepository;
import be.ephec.padel_backend.repository.MatchRepository;
import be.ephec.padel_backend.repository.MembreRepository;
import be.ephec.padel_backend.repository.ParticipationRepository;
import be.ephec.padel_backend.repository.TerrainRepository;
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
    private final TerrainRepository terrainRepository;
    private final MembreRepository membreRepository;
    private final ParticipationRepository participationRepository;

    @Autowired
    public ReservationService(MatchRepository matchRepository,
                              JourFermetureRepository jourFermetureRepository,
                              TerrainRepository terrainRepository,
                              MembreRepository membreRepository,
                              ParticipationRepository participationRepository) {
        this.matchRepository = matchRepository;
        this.jourFermetureRepository = jourFermetureRepository;
        this.terrainRepository = terrainRepository;
        this.membreRepository = membreRepository;
        this.participationRepository = participationRepository;
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

    private boolean isDansHorairesSite(Site site, LocalDateTime debut, LocalDateTime fin) {
        LocalDateTime ouvertureCeJour = debut.toLocalDate().atTime(site.getHeureOuverture());
        LocalDateTime fermetureCeJour = debut.toLocalDate().atTime(site.getHeureFermeture());

        return !debut.isBefore(ouvertureCeJour) && !fin.isAfter(fermetureCeJour);
    }

    private boolean isChevauchementAvecMatchExistant(Terrain terrain, LocalDateTime nouveauDebut) {
        LocalDateTime nouveauFinBloquee = nouveauDebut.plusMinutes(BLOCAGE_TOTAL_MINUTES);

        List<Match> matchesExistants = matchRepository.findAll().stream()
                .filter(m -> m.getTerrain().getIdTerrain().equals(terrain.getIdTerrain()))
                .toList();

        for (Match existant : matchesExistants) {
            LocalDateTime existantDebut = existant.getDateHeureDebut();
            LocalDateTime existantFinBloquee = existantDebut.plusMinutes(BLOCAGE_TOTAL_MINUTES);

            boolean chevauche = nouveauDebut.isBefore(existantFinBloquee)
                    && existantDebut.isBefore(nouveauFinBloquee);

            if (chevauche) {
                return true;
            }
        }
        return false;
    }

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

    public boolean isTerrainAutorise(Membre membre, Terrain terrain) {
        if (membre.getTypeMembre() != Membre.TypeMembre.SITE) {
            return true;
        }
        return membre.getSite() != null
                && membre.getSite().getIdSite().equals(terrain.getSite().getIdSite());
    }

    public boolean isPenaliteActive(Membre membre) {
        if (membre.getDateFinPenalite() == null) {
            return false;
        }
        return membre.getDateFinPenalite().isAfter(LocalDate.now());
    }

    /**
     * Orchestration complete : verifie toutes les regles puis cree le match
     * et la participation de l'organisateur. Leve une IllegalStateException
     * avec un message explicite si une regle n'est pas respectee.
     */
    public Match creerMatch(Integer idTerrain, LocalDateTime dateHeureDebut,
                            Match.Statut statut, String matriculeOrganisateur) {

        Terrain terrain = terrainRepository.findById(idTerrain)
                .orElseThrow(() -> new IllegalStateException("Terrain introuvable"));

        Membre organisateur = membreRepository.findById(matriculeOrganisateur)
                .orElseThrow(() -> new IllegalStateException("Membre introuvable"));

        if (isPenaliteActive(organisateur)) {
            throw new IllegalStateException("Le membre a une penalite active");
        }

        if (!isDelaiRespecte(organisateur, dateHeureDebut)) {
            throw new IllegalStateException("Delai de reservation non respecte pour ce type de membre");
        }

        if (!isTerrainAutorise(organisateur, terrain)) {
            throw new IllegalStateException("Ce membre ne peut pas reserver sur ce terrain");
        }

        if (!isCreneauDisponible(terrain, dateHeureDebut)) {
            throw new IllegalStateException("Creneau non disponible");
        }

        Match match = new Match();
        match.setTerrain(terrain);
        match.setDateHeureDebut(dateHeureDebut);
        match.setStatut(statut);
        Match matchSauvegarde = matchRepository.save(match);

        Participation participationOrganisateur = new Participation();
        participationOrganisateur.setMatch(matchSauvegarde);
        participationOrganisateur.setMembre(organisateur);
        participationOrganisateur.setEstOrganisateur(true);
        participationOrganisateur.setDateInscription(LocalDateTime.now());
        participationRepository.save(participationOrganisateur);

        return matchSauvegarde;
    }
}