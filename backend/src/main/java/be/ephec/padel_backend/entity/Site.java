package be.ephec.padel_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "SITE")
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_site")
    private Integer idSite;

    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @Column(name = "adresse", nullable = false, length = 255)
    private String adresse;

    @Column(name = "heure_ouverture", nullable = false)
    private java.time.LocalTime heureOuverture;

    @Column(name = "heure_fermeture", nullable = false)
    private java.time.LocalTime heureFermeture;

    // Constructeur vide requis par JPA
    public Site() {
    }

    // Getters et setters
    public Integer getIdSite() {
        return idSite;
    }

    public void setIdSite(Integer idSite) {
        this.idSite = idSite;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public java.time.LocalTime getHeureOuverture() {
        return heureOuverture;
    }

    public void setHeureOuverture(java.time.LocalTime heureOuverture) {
        this.heureOuverture = heureOuverture;
    }

    public java.time.LocalTime getHeureFermeture() {
        return heureFermeture;
    }

    public void setHeureFermeture(java.time.LocalTime heureFermeture) {
        this.heureFermeture = heureFermeture;
    }
}