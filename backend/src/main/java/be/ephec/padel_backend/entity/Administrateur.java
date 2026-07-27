package be.ephec.padel_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ADMINISTRATEUR")
public class Administrateur {

    public enum TypeAdmin {
        GLOBAL, SITE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_admin")
    private Integer idAdmin;

    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @Column(name = "prenom", nullable = false, length = 100)
    private String prenom;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_admin", nullable = false, length = 10)
    private TypeAdmin typeAdmin;

    @ManyToOne
    @JoinColumn(name = "id_site", nullable = true)
    private Site site;

    // Constructeur vide requis par JPA
    public Administrateur() {
    }

    // Getters et setters
    public Integer getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(Integer idAdmin) {
        this.idAdmin = idAdmin;
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

    public TypeAdmin getTypeAdmin() {
        return typeAdmin;
    }

    public void setTypeAdmin(TypeAdmin typeAdmin) {
        this.typeAdmin = typeAdmin;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }
}
