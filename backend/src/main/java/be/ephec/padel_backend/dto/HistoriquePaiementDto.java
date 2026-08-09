package be.ephec.padel_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class HistoriquePaiementDto {
    private Integer idParticipation;
    private Integer idPaiement;
    private BigDecimal montant;
    private LocalDateTime datePaiement;
    private String terrainNumero;
    private String siteNom;
    private LocalDateTime dateMatch;
    private String statutMatch;

    public HistoriquePaiementDto(Integer idParticipation, Integer idPaiement, BigDecimal montant, LocalDateTime datePaiement,
                                 String terrainNumero, String siteNom, LocalDateTime dateMatch, String statutMatch) {
        this.idParticipation = idParticipation;
        this.idPaiement = idPaiement;
        this.montant = montant;
        this.datePaiement = datePaiement;
        this.terrainNumero = terrainNumero;
        this.siteNom = siteNom;
        this.dateMatch = dateMatch;
        this.statutMatch = statutMatch;
    }

    public Integer getIdParticipation() { return idParticipation; }
    public Integer getIdPaiement() { return idPaiement; }
    public BigDecimal getMontant() { return montant; }
    public LocalDateTime getDatePaiement() { return datePaiement; }
    public String getTerrainNumero() { return terrainNumero; }
    public String getSiteNom() { return siteNom; }
    public LocalDateTime getDateMatch() { return dateMatch; }
    public String getStatutMatch() { return statutMatch; }
}