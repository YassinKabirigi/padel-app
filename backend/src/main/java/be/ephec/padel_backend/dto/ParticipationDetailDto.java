package be.ephec.padel_backend.dto;

import java.time.LocalDateTime;

public class ParticipationDetailDto {

    private Integer idParticipation;
    private String matriculeMembre;
    private String nomMembre;
    private LocalDateTime dateMatch;
    private String terrainNumero;
    private String siteNom;
    private String statutMatch;
    private Boolean estOrganisateur;
    private Boolean aPaye;
    private String montantPaye;

    public ParticipationDetailDto() {
    }

    public ParticipationDetailDto(Integer idParticipation, String matriculeMembre, String nomMembre,
                                  LocalDateTime dateMatch, String terrainNumero, String siteNom,
                                  String statutMatch, Boolean estOrganisateur, Boolean aPaye, String montantPaye) {
        this.idParticipation = idParticipation;
        this.matriculeMembre = matriculeMembre;
        this.nomMembre = nomMembre;
        this.dateMatch = dateMatch;
        this.terrainNumero = terrainNumero;
        this.siteNom = siteNom;
        this.statutMatch = statutMatch;
        this.estOrganisateur = estOrganisateur;
        this.aPaye = aPaye;
        this.montantPaye = montantPaye;
    }

    // Getters
    public Integer getIdParticipation() { return idParticipation; }
    public String getMatriculeMembre() { return matriculeMembre; }
    public String getNomMembre() { return nomMembre; }
    public LocalDateTime getDateMatch() { return dateMatch; }
    public String getTerrainNumero() { return terrainNumero; }
    public String getSiteNom() { return siteNom; }
    public String getStatutMatch() { return statutMatch; }
    public Boolean getEstOrganisateur() { return estOrganisateur; }
    public Boolean getAPaye() { return aPaye; }
    public String getMontantPaye() { return montantPaye; }
}