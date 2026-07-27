package be.ephec.padel_backend.service;

import be.ephec.padel_backend.entity.*;
        import be.ephec.padel_backend.repository.MatchRepository;
import be.ephec.padel_backend.repository.ParticipationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaiementService {

    private static final BigDecimal PRIX_PAR_JOUEUR = new BigDecimal("15.00");
    private static final int NB_JOUEURS_REQUIS = 4;
    private static final int PENALITE_JOURS = 7;

    private final MatchRepository matchRepository;
    private final ParticipationRepository participationRepository;

    @Autowired
    public PaiementService(MatchRepository matchRepository,
                           ParticipationRepository participationRepository) {
        this.matchRepository = matchRepository;
        this.participationRepository = participationRepository;
    }

    /**
     * Calcule le solde dû par un membre : la somme des participations
     * dont le paiement n'a pas encore été effectué.
     */
    public BigDecimal calculerSoldeDu(Membre membre) {
        List<Participation> participationsNonPayees = participationRepository.findAll().stream()
                .filter(p -> p.getMembre().getMatricule().equals(membre.getMatricule()))
                .filter(p -> p.getPaiement() == null)
                .toList();

        return PRIX_PAR_JOUEUR.multiply(BigDecimal.valueOf(participationsNonPayees.size()));
    }

    /**
     * Un membre ne peut pas réserver s'il a un solde dû (participations
     * impayées, hors la participation qu'il s'apprête à créer).
     */
    public boolean peutReserver(Membre membre) {
        return calculerSoldeDu(membre).compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * Crée un paiement couvrant une ou plusieurs participations
     * (permet de régler un solde reporté en même temps qu'une
     * nouvelle participation).
     */
    public Paiement effectuerPaiement(List<Participation> participationsACouvrir) {
        BigDecimal montantTotal = PRIX_PAR_JOUEUR
                .multiply(BigDecimal.valueOf(participationsACouvrir.size()));

        Paiement paiement = new Paiement();
        paiement.setMontant(montantTotal);
        paiement.setDatePaiement(LocalDateTime.now());
        // Le paiement doit être sauvegardé via PaiementRepository avant
        // d'être lié aux participations (id généré) — à faire dans le
        // controller/orchestration, ce service reste focalisé sur le calcul.

        for (Participation participation : participationsACouvrir) {
            participation.setPaiement(paiement);
        }

        return paiement;
    }

    /**
     * Vérifie, pour chaque match privé dont la date est demain, si le
     * nombre de joueurs requis (4) est atteint. Si non, bascule le
     * match en public et applique une pénalité à l'organisateur.
     * Destiné à être appelé une fois par jour (tâche planifiée).
     */
    public void verifierEtBasculerMatchesPrives() {
        LocalDate demain = LocalDate.now().plusDays(1);

        List<Match> matchesPrivesDemain = matchRepository.findAll().stream()
                .filter(m -> m.getStatut() == Match.Statut.PRIVE)
                .filter(m -> m.getDateHeureDebut().toLocalDate().equals(demain))
                .toList();

        for (Match match : matchesPrivesDemain) {
            long nbParticipants = participationRepository.findAll().stream()
                    .filter(p -> p.getMatch().getIdMatch().equals(match.getIdMatch()))
                    .count();

            if (nbParticipants < NB_JOUEURS_REQUIS) {
                match.setStatut(Match.Statut.PUBLIC);
                matchRepository.save(match);
                appliquerPenaliteOrganisateur(match);
            }
        }
    }

    /**
     * Applique une pénalité d'une semaine de délai supplémentaire à
     * l'organisateur du match (celui dont est_organisateur = true).
     */
    private void appliquerPenaliteOrganisateur(Match match) {
        participationRepository.findAll().stream()
                .filter(p -> p.getMatch().getIdMatch().equals(match.getIdMatch()))
                .filter(Participation::getEstOrganisateur)
                .findFirst()
                .ifPresent(participationOrganisateur -> {
                    Membre organisateur = participationOrganisateur.getMembre();
                    organisateur.setDateDebutPenalite(LocalDate.now());
                    organisateur.setDateFinPenalite(LocalDate.now().plusDays(PENALITE_JOURS));
                    organisateur.setMotifPenalite("Match prive non complete avant la veille");
                    // Sauvegarde via MembreRepository à faire dans le controller/orchestration
                });
    }
}