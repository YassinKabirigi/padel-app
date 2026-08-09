package be.ephec.padel_backend.dto;

import java.time.LocalDate;

public class PenaliteDto {
    private String matricule;
    private String nom;
    private String prenom;
    private LocalDate dateDebutPenalite;
    private LocalDate dateFinPenalite;
    private String motifPenalite;

    public PenaliteDto(String matricule, String nom, String prenom,
                       LocalDate dateDebutPenalite, LocalDate dateFinPenalite, String motifPenalite) {
        this.matricule = matricule;
        this.nom = nom;
        this.prenom = prenom;
        this.dateDebutPenalite = dateDebutPenalite;
        this.dateFinPenalite = dateFinPenalite;
        this.motifPenalite = motifPenalite;
    }

    public String getMatricule() { return matricule; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public LocalDate getDateDebutPenalite() { return dateDebutPenalite; }
    public LocalDate getDateFinPenalite() { return dateFinPenalite; }
    public String getMotifPenalite() { return motifPenalite; }
}