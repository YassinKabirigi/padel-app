package be.ephec.padel_backend.dto;

import java.time.LocalDateTime;

public class MatchDisponibleDto {
    private Integer idMatch;
    private LocalDateTime dateHeureDebut;
    private String terrainNumero;
    private String siteNom;
    private String statut;
    private long nbParticipants;
    private boolean dejaParticipant;
    private boolean peutRejoindre;

    public MatchDisponibleDto(Integer idMatch, LocalDateTime dateHeureDebut, String terrainNumero,
                              String siteNom, String statut, long nbParticipants,
                              boolean dejaParticipant, boolean peutRejoindre) {
        this.idMatch = idMatch;
        this.dateHeureDebut = dateHeureDebut;
        this.terrainNumero = terrainNumero;
        this.siteNom = siteNom;
        this.statut = statut;
        this.nbParticipants = nbParticipants;
        this.dejaParticipant = dejaParticipant;
        this.peutRejoindre = peutRejoindre;
    }

    public Integer getIdMatch() { return idMatch; }
    public LocalDateTime getDateHeureDebut() { return dateHeureDebut; }
    public String getTerrainNumero() { return terrainNumero; }
    public String getSiteNom() { return siteNom; }
    public String getStatut() { return statut; }
    public long getNbParticipants() { return nbParticipants; }
    public boolean isDejaParticipant() { return dejaParticipant; }
    public boolean isPeutRejoindre() { return peutRejoindre; }
}
