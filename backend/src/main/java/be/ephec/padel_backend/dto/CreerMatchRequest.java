package be.ephec.padel_backend.dto;

import java.util.List;

public class CreerMatchRequest {

    private Integer idTerrain;
    private String dateHeureDebut;
    private String statut;
    private String matriculeOrganisateur;
    private List<String> matriculesCoequipiers;

    public CreerMatchRequest() {
    }

    public Integer getIdTerrain() {
        return idTerrain;
    }

    public void setIdTerrain(Integer idTerrain) {
        this.idTerrain = idTerrain;
    }

    public String getDateHeureDebut() {
        return dateHeureDebut;
    }

    public void setDateHeureDebut(String dateHeureDebut) {
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

    public List<String> getMatriculesCoequipiers() {
        return matriculesCoequipiers;
    }

    public void setMatriculesCoequipiers(List<String> matriculesCoequipiers) {
        this.matriculesCoequipiers = matriculesCoequipiers;
    }
}