package be.ephec.padel_backend.dto;

public class MonProfilDto {
    private String matricule;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String typeMembre;
    private String siteNom;

    public MonProfilDto(String matricule, String nom, String prenom, String email,
                        String telephone, String typeMembre, String siteNom) {
        this.matricule = matricule;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.typeMembre = typeMembre;
        this.siteNom = siteNom;
    }

    public String getMatricule() { return matricule; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public String getTelephone() { return telephone; }
    public String getTypeMembre() { return typeMembre; }
    public String getSiteNom() { return siteNom; }
}