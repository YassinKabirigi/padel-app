package be.ephec.padel_backend.controller;

import be.ephec.padel_backend.dto.LoginRequest;
import be.ephec.padel_backend.dto.LoginResponse;
import be.ephec.padel_backend.entity.Membre;
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
    private final JwtService jwtService;

    @Autowired
    public AuthController(MembreRepository membreRepository, JwtService jwtService) {
        this.membreRepository = membreRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Membre membre = membreRepository.findById(request.getMatricule()).orElse(null);

        if (membre == null) {
            return ResponseEntity.status(401).body("Matricule inconnu");
        }

        String typeMembre = membre.getTypeMembre().name();
        String token = jwtService.genererToken(membre.getMatricule(), typeMembre);

        LoginResponse response = new LoginResponse(token, membre.getMatricule(), typeMembre);
        return ResponseEntity.ok(response);
    }
}