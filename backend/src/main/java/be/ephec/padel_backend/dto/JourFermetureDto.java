package be.ephec.padel_backend.dto;

import java.time.LocalDate;

public class JourFermetureDto {
    private Integer idFermeture;
    private LocalDate dateFermeture;
    private String motif;
    private Integer idSite;
    private String siteNom;

    public JourFermetureDto() {
    }

    public JourFermetureDto(Integer idFermeture, LocalDate dateFermeture, String motif, Integer idSite, String siteNom) {
        this.idFermeture = idFermeture;
        this.dateFermeture = dateFermeture;
        this.motif = motif;
        this.idSite = idSite;
        this.siteNom = siteNom;
    }

    public Integer getIdFermeture() { return idFermeture; }
    public void setIdFermeture(Integer idFermeture) { this.idFermeture = idFermeture; }
    public LocalDate getDateFermeture() { return dateFermeture; }
    public void setDateFermeture(LocalDate dateFermeture) { this.dateFermeture = dateFermeture; }
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
    public Integer getIdSite() { return idSite; }
    public void setIdSite(Integer idSite) { this.idSite = idSite; }
    public String getSiteNom() { return siteNom; }
    public void setSiteNom(String siteNom) { this.siteNom = siteNom; }
}