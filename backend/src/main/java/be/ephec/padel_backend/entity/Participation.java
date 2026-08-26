package be.ephec.padel_backend.entity;

import jakarta.persistence.*;
        import java.time.LocalDateTime;

@Entity
@Table(name = "PARTICIPATION")
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_participation")
    private Integer idParticipation;

    @Column(name = "date_inscription", nullable = false)
    private LocalDateTime dateInscription;

    @Column(name = "est_organisateur", nullable = false)
    private Boolean estOrganisateur = false;

    @ManyToOne
    @JoinColumn(name = "matricule", nullable = false)
    private Membre membre;

    @ManyToOne
    @JoinColumn(name = "id_match", nullable = false)
    private Match match;

    @ManyToOne
    @JoinColumn(name = "id_paiement", nullable = true)
    private Paiement paiement;

    // Constructeur vide requis par JPA
    public Participation() {
    }

    // Getters et setters
    public Integer getIdParticipation() {
        return idParticipation;
    }

    public void setIdParticipation(Integer idParticipation) {
        this.idParticipation = idParticipation;
    }

    public LocalDateTime getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDateTime dateInscription) {
        this.dateInscription = dateInscription;
    }

    public Boolean getEstOrganisateur() {
        return estOrganisateur;
    }

    public void setEstOrganisateur(Boolean estOrganisateur) {
        this.estOrganisateur = estOrganisateur;
    }

    public Membre getMembre() {
        return membre;
    }

    public void setMembre(Membre membre) {
        this.membre = membre;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Paiement getPaiement() {
        return paiement;
    }

    public void setPaiement(Paiement paiement) {
        this.paiement = paiement;
    }
}