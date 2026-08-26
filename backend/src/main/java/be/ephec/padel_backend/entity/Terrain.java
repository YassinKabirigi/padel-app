package be.ephec.padel_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "TERRAIN")
public class Terrain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_terrain")
    private Integer idTerrain;

    @Column(name = "numero", nullable = false, length = 20)
    private String numero;

    @ManyToOne
    @JoinColumn(name = "id_site", nullable = false)
    private Site site;

    // Constructeur vide requis par JPA
    public Terrain() {
    }

    // Getters et setters
    public Integer getIdTerrain() {
        return idTerrain;
    }

    public void setIdTerrain(Integer idTerrain) {
        this.idTerrain = idTerrain;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }
}