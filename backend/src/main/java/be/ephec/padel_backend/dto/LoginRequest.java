package be.ephec.padel_backend.dto;

public class LoginRequest {

    private String matricule;

    public LoginRequest() {
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }
}