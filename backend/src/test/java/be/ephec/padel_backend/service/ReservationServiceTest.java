package be.ephec.padel_backend.service;

import be.ephec.padel_backend.entity.Membre;
import be.ephec.padel_backend.entity.Site;
import be.ephec.padel_backend.entity.Terrain;
import be.ephec.padel_backend.repository.JourFermetureRepository;
import be.ephec.padel_backend.repository.MatchRepository;
import be.ephec.padel_backend.repository.MembreRepository;
import be.ephec.padel_backend.repository.ParticipationRepository;
import be.ephec.padel_backend.repository.TerrainRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        // Repositories non utilises directement par les methodes testees ici,
        // on peut passer null car ces methodes ne font pas d'appel base de donnees
        reservationService = new ReservationService(
                null, null, null, null, null
        );
    }

    @Nested
    class TestsDelaiReservation {

        @Test
        void membreGlobal_delaiRespecte_21joursOuPlus() {
            Membre membre = new Membre();
            membre.setTypeMembre(Membre.TypeMembre.GLOBAL);

            LocalDateTime dateMatch = LocalDateTime.now().plusDays(21);

            assertTrue(reservationService.isDelaiRespecte(membre, dateMatch));
        }

        @Test
        void membreGlobal_delaiNonRespecte_moinsDe21jours() {
            Membre membre = new Membre();
            membre.setTypeMembre(Membre.TypeMembre.GLOBAL);

            LocalDateTime dateMatch = LocalDateTime.now().plusDays(10);

            assertFalse(reservationService.isDelaiRespecte(membre, dateMatch));
        }

        @Test
        void membreSite_delaiRespecte_14jours() {
            Membre membre = new Membre();
            membre.setTypeMembre(Membre.TypeMembre.SITE);

            LocalDateTime dateMatch = LocalDateTime.now().plusDays(14);

            assertTrue(reservationService.isDelaiRespecte(membre, dateMatch));
        }

        @Test
        void membreLibre_delaiRespecte_5jours() {
            Membre membre = new Membre();
            membre.setTypeMembre(Membre.TypeMembre.LIBRE);

            LocalDateTime dateMatch = LocalDateTime.now().plusDays(5);

            assertTrue(reservationService.isDelaiRespecte(membre, dateMatch));
        }

        @Test
        void membreLibre_delaiNonRespecte_moinsDe5jours() {
            Membre membre = new Membre();
            membre.setTypeMembre(Membre.TypeMembre.LIBRE);

            LocalDateTime dateMatch = LocalDateTime.now().plusDays(2);

            assertFalse(reservationService.isDelaiRespecte(membre, dateMatch));
        }
    }

    @Nested
    class TestsTerrainAutorise {

        @Test
        void membreGlobal_peutReserverSurNimporteQuelTerrain() {
            Membre membre = new Membre();
            membre.setTypeMembre(Membre.TypeMembre.GLOBAL);

            Site site = new Site();
            site.setIdSite(1);
            Terrain terrain = new Terrain();
            terrain.setSite(site);

            assertTrue(reservationService.isTerrainAutorise(membre, terrain));
        }

        @Test
        void membreSite_peutReserverSurSonPropreSite() {
            Site site = new Site();
            site.setIdSite(1);

            Membre membre = new Membre();
            membre.setTypeMembre(Membre.TypeMembre.SITE);
            membre.setSite(site);

            Terrain terrain = new Terrain();
            terrain.setSite(site);

            assertTrue(reservationService.isTerrainAutorise(membre, terrain));
        }

        @Test
        void membreSite_neePeutPasReserverSurUnAutreSite() {
            Site sonSite = new Site();
            sonSite.setIdSite(1);

            Site autreSite = new Site();
            autreSite.setIdSite(2);

            Membre membre = new Membre();
            membre.setTypeMembre(Membre.TypeMembre.SITE);
            membre.setSite(sonSite);

            Terrain terrain = new Terrain();
            terrain.setSite(autreSite);

            assertFalse(reservationService.isTerrainAutorise(membre, terrain));
        }
    }

    @Nested
    class TestsPenalite {

        @Test
        void membreSansPenalite_penaliteNonActive() {
            Membre membre = new Membre();
            membre.setDateFinPenalite(null);

            assertFalse(reservationService.isPenaliteActive(membre));
        }

        @Test
        void membreAvecPenaliteFuture_penaliteActive() {
            Membre membre = new Membre();
            membre.setDateFinPenalite(LocalDate.now().plusDays(3));

            assertTrue(reservationService.isPenaliteActive(membre));
        }

        @Test
        void membreAvecPenalitePassee_penaliteNonActive() {
            Membre membre = new Membre();
            membre.setDateFinPenalite(LocalDate.now().minusDays(1));

            assertFalse(reservationService.isPenaliteActive(membre));
        }
    }
}