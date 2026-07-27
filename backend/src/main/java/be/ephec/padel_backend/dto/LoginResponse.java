package be.ephec.padel_backend.dto;

public class LoginResponse {

    private String token;
    private String matricule;
    private String typeMembre;

    public LoginResponse(String token, String matricule, String typeMembre) {
        this.token = token;
        this.matricule = matricule;
        this.typeMembre = typeMembre;
    }

    public String getToken() {
        return token;
    }

    public String getMatricule() {
        return matricule;
    }

    public String getTypeMembre() {
        return typeMembre;
    }
}