package be.ephec.padel_backend.controller;

import be.ephec.padel_backend.entity.Membre;
import be.ephec.padel_backend.entity.Site;
import be.ephec.padel_backend.entity.Terrain;
import be.ephec.padel_backend.repository.MembreRepository;
import be.ephec.padel_backend.repository.SiteRepository;
import be.ephec.padel_backend.repository.TerrainRepository;
import be.ephec.padel_backend.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MatchControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private TerrainRepository terrainRepository;

    @Autowired
    private MembreRepository membreRepository;

    @Autowired
    private JwtService jwtService;

    private String token;
    private Integer idTerrainTest;
    private String matriculeTest;

    @BeforeEach
    void setUp() {
        Site site = new Site();
        site.setNom("Site Test Integration");
        site.setAdresse("Rue de Test 1");
        site.setHeureOuverture(LocalTime.of(8, 0));
        site.setHeureFermeture(LocalTime.of(22, 0));
        site = siteRepository.save(site);

        Terrain terrain = new Terrain();
        terrain.setNumero("Terrain Test");
        terrain.setSite(site);
        terrain = terrainRepository.save(terrain);
        idTerrainTest = terrain.getIdTerrain();

        matriculeTest = "G" + (System.currentTimeMillis() % 10000);
        Membre membre = new Membre();
        membre.setMatricule(matriculeTest);
        membre.setNom("TestNom");
        membre.setPrenom("TestPrenom");
        membre.setEmail("test.integration@test.be");
        membre.setTelephone("0400000000");
        membre.setDateInscription(LocalDate.now());
        membre.setTypeMembre(Membre.TypeMembre.GLOBAL);
        membreRepository.save(membre);

        token = jwtService.genererToken(matriculeTest, "GLOBAL");
    }

    @Test
    void creerMatch_happyFlow_retourne201() throws Exception {
        LocalDateTime dateHeureDebut = LocalDateTime.now().plusDays(25).withHour(10).withMinute(0);

        Map<String, Object> body = new HashMap<>();
        body.put("idTerrain", idTerrainTest);
        body.put("dateHeureDebut", dateHeureDebut.toString());
        body.put("statut", "PRIVE");

        mockMvc.perform(post("/api/matches")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated());
    }

    @Test
    void creerMatch_sansToken_retourne403() throws Exception {
        LocalDateTime dateHeureDebut = LocalDateTime.now().plusDays(25).withHour(11).withMinute(0);

        Map<String, Object> body = new HashMap<>();
        body.put("idTerrain", idTerrainTest);
        body.put("dateHeureDebut", dateHeureDebut.toString());
        body.put("statut", "PRIVE");

        mockMvc.perform(post("/api/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isForbidden());
    }
}