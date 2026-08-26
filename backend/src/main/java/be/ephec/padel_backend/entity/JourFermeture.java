package be.ephec.padel_backend.entity;

import jakarta.persistence.*;
        import java.time.LocalDate;

@Entity
@Table(name = "JOUR_FERMETURE")
public class JourFermeture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fermeture")
    private Integer idFermeture;

    @Column(name = "date_fermeture", nullable = false)
    private LocalDate dateFermeture;

    @Column(name = "motif", length = 255)
    private String motif;

    @ManyToOne
    @JoinColumn(name = "id_site", nullable = true)
    private Site site;

    // Constructeur vide requis par JPA
    public JourFermeture() {
    }

    // Getters et setters
    public Integer getIdFermeture() {
        return idFermeture;
    }

    public void setIdFermeture(Integer idFermeture) {
        this.idFermeture = idFermeture;
    }

    public LocalDate getDateFermeture() {
        return dateFermeture;
    }

    public void setDateFermeture(LocalDate dateFermeture) {
        this.dateFermeture = dateFermeture;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }
}