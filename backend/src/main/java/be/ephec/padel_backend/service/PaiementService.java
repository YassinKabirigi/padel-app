package be.ephec.padel_backend.service;

import be.ephec.padel_backend.entity.*;
import be.ephec.padel_backend.dto.HistoriquePaiementDto;
import be.ephec.padel_backend.dto.MesPaiementsDto;
import be.ephec.padel_backend.dto.MesStatistiquesDto;
import be.ephec.padel_backend.repository.MatchRepository;
import be.ephec.padel_backend.repository.PaiementRepository;
import be.ephec.padel_backend.repository.MembreRepository;
import be.ephec.padel_backend.repository.ParticipationRepository;
import org.springframework.scheduling.annotation.Scheduled;
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

    private final MembreRepository membreRepository;
    private final PaiementRepository paiementRepository;
    private final MatchRepository matchRepository;
    private final ParticipationRepository participationRepository;

    @Autowired
    public PaiementService(MembreRepository membreRepository,
                           MatchRepository matchRepository,
                           ParticipationRepository participationRepository,
                           PaiementRepository paiementRepository) {
        this.membreRepository = membreRepository;
        this.matchRepository = matchRepository;
        this.participationRepository = participationRepository;
        this.paiementRepository = paiementRepository;
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
    @Scheduled(cron = "0 0 0 * * *")  // tous les jours à minuit
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
                    membreRepository.save(organisateur);
                });
    }
    public MesPaiementsDto getMesPaiements(String matricule) {
        List<Participation> participations = participationRepository.findAll().stream()
                .filter(p -> p.getMembre().getMatricule().equals(matricule))
                .toList();
        java.math.BigDecimal du = PRIX_PAR_JOUEUR.multiply(java.math.BigDecimal.valueOf(participations.size()));
        java.math.BigDecimal paye = participations.stream()
                .filter(p -> p.getPaiement() != null)
                .map(p -> PRIX_PAR_JOUEUR)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        return new MesPaiementsDto(du, paye, du.subtract(paye));
    }

    public MesStatistiquesDto getMesStatistiques(String matricule) {
        List<Participation> participations = participationRepository.findAll().stream()
                .filter(p -> p.getMembre().getMatricule().equals(matricule))
                .toList();
        LocalDateTime now = LocalDateTime.now();
        long joues = participations.stream().filter(p -> p.getMatch().getDateHeureDebut().isBefore(now)).count();
        long aVenir = participations.stream().filter(p -> p.getMatch().getDateHeureDebut().isAfter(now)).count();
        long prives = participations.stream().filter(p -> p.getMatch().getStatut() == Match.Statut.PRIVE).count();
        long publics = participations.stream().filter(p -> p.getMatch().getStatut() == Match.Statut.PUBLIC).count();
        return new MesStatistiquesDto(joues, aVenir, prives, publics);
    }

    public List<HistoriquePaiementDto> getHistoriquePaiements(String matricule) {
        return participationRepository.findAll().stream()
                .filter(p -> p.getMembre().getMatricule().equals(matricule))
                .map(p -> {
                    Paiement paiement = p.getPaiement();
                    java.math.BigDecimal montant = paiement != null ? paiement.getMontant() : PRIX_PAR_JOUEUR;
                    LocalDateTime datePaiement = paiement != null ? paiement.getDatePaiement() : null;
                    return new HistoriquePaiementDto(
                            p.getIdParticipation(),
                            paiement != null ? paiement.getIdPaiement() : null,
                            montant, datePaiement,
                            p.getMatch().getTerrain().getNumero(),
                            p.getMatch().getTerrain().getSite().getNom(),
                            p.getMatch().getDateHeureDebut(),
                            p.getMatch().getStatut().name()
                    );
                })
                .sorted((a, b) -> b.getDateMatch().compareTo(a.getDateMatch()))
                .toList();
    }

    public void payerParticipation(Integer idParticipation, String matricule) {
        Participation participation = participationRepository.findById(idParticipation)
                .orElseThrow(() -> new IllegalStateException("Participation introuvable"));
        if (!participation.getMembre().getMatricule().equals(matricule)) {
            throw new IllegalStateException("Vous ne pouvez payer que vos propres participations");
        }
        if (participation.getPaiement() != null) {
            throw new IllegalStateException("Cette participation est déjà payée");
        }
        Paiement paiement = effectuerPaiement(List.of(participation));
        Paiement sauvegarde = paiementRepository.save(paiement);
        participation.setPaiement(sauvegarde);
        participationRepository.save(participation);
    }

}
