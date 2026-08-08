package be.ephec.padel_backend.controller;

import be.ephec.padel_backend.dto.LoginRequest;
import be.ephec.padel_backend.dto.LoginResponse;
import be.ephec.padel_backend.entity.Administrateur;
import be.ephec.padel_backend.entity.Membre;
import be.ephec.padel_backend.repository.AdministrateurRepository;
import be.ephec.padel_backend.repository.MembreRepository;
import be.ephec.padel_backend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final MembreRepository membreRepository;
    private final AdministrateurRepository administrateurRepository;
    private final JwtService jwtService;

    @Autowired
    public AuthController(MembreRepository membreRepository,
                          AdministrateurRepository administrateurRepository,
                          JwtService jwtService) {
        this.membreRepository = membreRepository;
        this.administrateurRepository = administrateurRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String identifiant = request.getMatricule();

        // On tente d'abord de trouver un Membre
        Membre membre = membreRepository.findById(identifiant).orElse(null);
        if (membre != null) {
            String token = jwtService.genererToken(membre.getMatricule(), membre.getTypeMembre().name());
            return ResponseEntity.ok(new LoginResponse(token, membre.getMatricule(), membre.getTypeMembre().name()));
        }

        // Sinon, on cherche un Administrateur (identifiant = id_admin sous forme de chaine, ex "ADMIN-1")
        if (identifiant.startsWith("ADMIN-")) {
            try {
                Integer idAdmin = Integer.parseInt(identifiant.substring(6));
                Administrateur admin = administrateurRepository.findById(idAdmin).orElse(null);
                if (admin != null) {
                    String role = "ADMIN_" + admin.getTypeAdmin().name(); // ADMIN_GLOBAL ou ADMIN_SITE
                    String token = jwtService.genererToken(identifiant, role);
                    return ResponseEntity.ok(new LoginResponse(token, identifiant, role));
                }
            } catch (NumberFormatException e) {
                // format invalide, on tombe sur le 401 ci-dessous
            }
        }

        return ResponseEntity.status(401).body("Identifiant inconnu");
    }
}