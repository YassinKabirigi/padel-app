package be.ephec.padel_backend.service;

import be.ephec.padel_backend.dto.LoginRequest;
import be.ephec.padel_backend.dto.LoginResponse;
import be.ephec.padel_backend.entity.Administrateur;
import be.ephec.padel_backend.entity.Membre;
import be.ephec.padel_backend.repository.AdministrateurRepository;
import be.ephec.padel_backend.repository.MembreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final MembreRepository membreRepository;
    private final AdministrateurRepository administrateurRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(MembreRepository membreRepository,
                       AdministrateurRepository administrateurRepository,
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder) {
        this.membreRepository = membreRepository;
        this.administrateurRepository = administrateurRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Authentifie un utilisateur (membre ou administrateur) et retourne un token JWT.
     * Lève IllegalArgumentException si l'identifiant est inconnu ou le mot de passe incorrect.
     */
    public LoginResponse login(LoginRequest request) {
        String identifiant = request.getMatricule();

        // Tentative d'authentification en tant que Membre
        Membre membre = membreRepository.findById(identifiant).orElse(null);
        if (membre != null) {
            verifierMotDePasse(request.getMotDePasse(), membre.getMotDePasse());
            String token = jwtService.genererToken(membre.getMatricule(), membre.getTypeMembre().name());
            return new LoginResponse(token, membre.getMatricule(), membre.getTypeMembre().name());
        }

        // Tentative d'authentification en tant qu'Administrateur (identifiant = ADMIN-{id})
        if (identifiant.startsWith("ADMIN-")) {
            try {
                Integer idAdmin = Integer.parseInt(identifiant.substring(6));
                Administrateur admin = administrateurRepository.findById(idAdmin).orElse(null);
                if (admin != null) {
                    verifierMotDePasse(request.getMotDePasse(), admin.getMotDePasse());
                    String role = "ADMIN_" + admin.getTypeAdmin().name();
                    String token = jwtService.genererToken(identifiant, role);
                    return new LoginResponse(token, identifiant, role);
                }
            } catch (NumberFormatException e) {
                // format invalide, on tombe sur l'exception ci-dessous
            }
        }

        throw new IllegalArgumentException("Identifiant ou mot de passe incorrect");
    }

    private void verifierMotDePasse(String motDePasseSaisi, String motDePasseStocke) {
        if (motDePasseStocke == null || !passwordEncoder.matches(motDePasseSaisi, motDePasseStocke)) {
            throw new IllegalArgumentException("Identifiant ou mot de passe incorrect");
        }
    }
}
