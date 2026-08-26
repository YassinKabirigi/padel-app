package be.ephec.padel_backend.service;

import be.ephec.padel_backend.dto.LoginRequest;
import be.ephec.padel_backend.dto.LoginResponse;
import be.ephec.padel_backend.entity.Membre;
import be.ephec.padel_backend.repository.AdministrateurRepository;
import be.ephec.padel_backend.repository.MembreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private MembreRepository membreRepository;
    @Mock private AdministrateurRepository administrateurRepository;
    @Mock private JwtService jwtService;
    @Mock private PasswordEncoder passwordEncoder;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(membreRepository, administrateurRepository,
                jwtService, passwordEncoder);
    }

    @Test
    void login_membreAvecBonMotDePasse_retourneToken() {
        Membre membre = new Membre();
        membre.setMatricule("G1042");
        membre.setTypeMembre(Membre.TypeMembre.GLOBAL);
        membre.setMotDePasse("$2a$10$hash");

        when(membreRepository.findById("G1042")).thenReturn(Optional.of(membre));
        when(passwordEncoder.matches("padel2026", "$2a$10$hash")).thenReturn(true);
        when(jwtService.genererToken("G1042", "GLOBAL")).thenReturn("token-test");

        LoginRequest request = new LoginRequest();
        request.setMatricule("G1042");
        request.setMotDePasse("padel2026");

        LoginResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("G1042", response.getMatricule());
        assertEquals("token-test", response.getToken());
    }

    @Test
    void login_mauvaisMotDePasse_leveException() {
        Membre membre = new Membre();
        membre.setMatricule("G1042");
        membre.setTypeMembre(Membre.TypeMembre.GLOBAL);
        membre.setMotDePasse("$2a$10$hash");

        when(membreRepository.findById("G1042")).thenReturn(Optional.of(membre));
        when(passwordEncoder.matches("mauvais", "$2a$10$hash")).thenReturn(false);

        LoginRequest request = new LoginRequest();
        request.setMatricule("G1042");
        request.setMotDePasse("mauvais");

        assertThrows(IllegalArgumentException.class, () -> authService.login(request));
    }

    @Test
    void login_identifiantInconnu_leveException() {
        when(membreRepository.findById("INCONNU")).thenReturn(Optional.empty());

        LoginRequest request = new LoginRequest();
        request.setMatricule("INCONNU");
        request.setMotDePasse("padel2026");

        assertThrows(IllegalArgumentException.class, () -> authService.login(request));
    }
}
