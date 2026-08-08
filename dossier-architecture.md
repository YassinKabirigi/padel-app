# Dossier d'architecture — Padel App

## Vue d'ensemble

Application de gestion de terrains de padel, développée dans le cadre des cours SGBD et PDW (EPHEC). Un seul dépôt Git contient le backend et le frontend.

Repo : https://github.com/YassinKabirigi/padel-app

## Stack technique

| Couche | Technologie |
|---|---|
| Backend | Spring Boot 4.0.7, Java 21, Maven |
| Base de données | SQL Server 2022 (conteneur Docker) |
| Migrations | Flyway |
| Frontend | Angular 21, TypeScript, Angular Material |
| Authentification | JWT (jjwt 0.12.6), Spring Security |
| Tests backend | JUnit 5 |
| Tests frontend | Vitest (unitaires), Cypress (E2E) |

## Architecture backend — en couches

be.ephec.padel_backend/
├── entity/ Entités JPA (8) : Site, Terrain, JourFermeture, Match,
│ Membre, Paiement, Participation, Administrateur
├── repository/ Interfaces Spring Data JPA (8)
├── service/ Logique métier (Site, Terrain, Reservation, Paiement,
│ Membre, Administrateur, Jwt)
├── security/ JwtAuthFilter, SecurityConfig (CORS, routes protégées)
├── exception/ GlobalExceptionHandler (@ControllerAdvice)
├── dto/ Objets de transfert (LoginRequest/Response, CreerMatchRequest)
├── controller/ Endpoints REST (Auth, Site, Terrain, Match, Membre, Administrateur)


Principe : chaque controller délègue systématiquement à un service dédié, même pour du CRUD simple — homogénéité architecturale sur l'ensemble du projet.

## Architecture frontend — en couches

src/app/
├── core/
│ ├── services/ Site, Terrain, Match, Auth (appels HTTP)
│ ├── guards/ authGuard (protection des routes)
│ └── interceptors/ authInterceptor (injection automatique du JWT)
├── features/
│ ├── login/ Écran de connexion
│ ├── reservation/ Liste terrains + formulaire de réservation
│ └── admin/ Gestion sites/terrains

## Authentification

Flux : matricule → `POST /api/auth/login` → JWT (contient matricule + typeMembre) → stocké en mémoire côté client (jamais en localStorage, protection contre le XSS) → attaché automatiquement à chaque requête via l'interceptor → vérifié par `JwtAuthFilter` côté backend.

Routes publiques : `/api/auth/**`. Toutes les autres routes nécessitent une authentification valide.

## Modèle de données

Voir `analyse-mcd.md` pour le MCD, le MLD complet, les justifications de modélisation et les contraintes métier en langage usuel.

## Logique métier principale

- Disponibilité des créneaux : horaires du site, jours de fermeture (globale ou par site), battement de 15 min entre matches (bloque réellement 105 min par match existant)
- Délais de réservation par type de membre : Global 21j, Site 14j, Libre 5j
- Un MembreSite ne peut réserver que sur son site de rattachement
- Un paiement peut couvrir plusieurs participations (solde reporté)

## Tests

- Backend : 12 tests unitaires (`ReservationServiceTest`, 3 groupes `@Nested`)
- Frontend : 13 tests unitaires (services, guard, interceptor, composants)
- E2E : 1 test Cypress du parcours principal (connexion → réservation), isolé via date/heure aléatoires pour être rejouable sans conflit

## API — Documentation OpenAPI/Swagger

Une fois le backend démarré : http://localhost:8080/swagger-ui.html

## Sécurité par rôles

Deux familles de rôles : Membre (GLOBAL/SITE/LIBRE) et Administrateur
(ADMIN_GLOBAL/ADMIN_SITE), tous deux authentifiés via JWT. Les opérations
de gestion (sites, terrains, membres, administrateurs, consultation des
réservations) sont réservées aux rôles ADMIN_*, vérifié au niveau de
SecurityConfig via hasAnyAuthority()/hasAuthority() par route et méthode
HTTP.

## Tests

- Backend : 12 tests unitaires + 2 tests d'intégration (@SpringBootTest,
  base de données réelle)
- Frontend : 13 tests unitaires
- E2E : 1 test Cypress isolé (parcours complet)

## Documentation complémentaire

- `document-exploitation.md` : instructions de démarrage, prérequis, commandes
- `analyse-mcd.md` : MCD, MLD, justifications de modélisation (pour le cours SGBD)
- 