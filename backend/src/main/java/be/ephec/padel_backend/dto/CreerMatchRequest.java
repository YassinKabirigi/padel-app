package be.ephec.padel_backend.dto;

import java.time.LocalDateTime;

public class CreerMatchRequest {

    private Integer idTerrain;
    private LocalDateTime dateHeureDebut;
    private String statut; // "PRIVE" ou "PUBLIC"
    private String matriculeOrganisateur;

    public CreerMatchRequest() {
    }

    public Integer getIdTerrain() {
        return idTerrain;
    }

    public void setIdTerrain(Integer idTerrain) {
        this.idTerrain = idTerrain;
    }

    public LocalDateTime getDateHeureDebut() {
        return dateHeureDebut;
    }

    public void setDateHeureDebut(LocalDateTime dateHeureDebut) {
        this.dateHeureDebut = dateHeureDebut;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getMatriculeOrganisateur() {
        return matriculeOrganisateur;
    }

    public void setMatriculeOrganisateur(String matriculeOrganisateur) {
        this.matriculeOrganisateur = matriculeOrganisateur;
    }
}