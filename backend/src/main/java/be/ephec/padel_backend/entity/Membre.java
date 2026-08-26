package be.ephec.padel_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "MEMBRE")
public class Membre {

    public enum TypeMembre {
        GLOBAL, SITE, LIBRE
    }

    @Id
    @Column(name = "matricule", length = 10)
    private String matricule;

    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @Column(name = "prenom", nullable = false, length = 100)
    private String prenom;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "telephone", length = 20)
    private String telephone;

    @Column(name = "date_inscription", nullable = false)
    private LocalDate dateInscription;

    @Column(name = "date_debut_penalite")
    private LocalDate dateDebutPenalite;

    @Column(name = "date_fin_penalite")
    private LocalDate dateFinPenalite;

    @Column(name = "motif_penalite", length = 255)
    private String motifPenalite;

    @Column(name = "mot_de_passe")
    private String motDePasse;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_membre", nullable = false, length = 10)
    private TypeMembre typeMembre;

    @ManyToOne
    @JoinColumn(name = "id_site", nullable = true)
    private Site site;

    // Constructeur vide requis par JPA
    public Membre() {
    }

    // Getters et setters
    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public LocalDate getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDate dateInscription) {
        this.dateInscription = dateInscription;
    }

    public LocalDate getDateDebutPenalite() {
        return dateDebutPenalite;
    }

    public void setDateDebutPenalite(LocalDate dateDebutPenalite) {
        this.dateDebutPenalite = dateDebutPenalite;
    }

    public LocalDate getDateFinPenalite() {
        return dateFinPenalite;
    }

    public void setDateFinPenalite(LocalDate dateFinPenalite) {
        this.dateFinPenalite = dateFinPenalite;
    }

    public String getMotifPenalite() {
        return motifPenalite;
    }

    public void setMotifPenalite(String motifPenalite) {
        this.motifPenalite = motifPenalite;
    }

    public TypeMembre getTypeMembre() {
        return typeMembre;
    }

    public void setTypeMembre(TypeMembre typeMembre) {
        this.typeMembre = typeMembre;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
}
