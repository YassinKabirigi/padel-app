# Padel-App — EPHEC SGBD 2024–2025

Application web de gestion et réservation de terrains de padel.  
**Étudiant :** Yassin Kabirigi  
**Repository :** https://github.com/YassinKabirigi/padel-app

---

## Stack technique

| Couche | Technologie |
|--------|-------------|
| Backend | Java 21 / Spring Boot 4.0.7 |
| Frontend | Angular 19 (port 4200) |
| Base de données | SQL Server 2022 (Docker) |
| Migrations | Flyway (V1 → V4) |
| Sécurité | Spring Security + JWT (HS256) + BCrypt |

---

## Démarrage (test à froid)

### Prérequis
- Docker Desktop (en cours d'exécution)
- Java 21
- Node.js 20+

### 1. Cloner le projet
```bash
git clone https://github.com/YassinKabirigi/padel-app.git
cd padel-app
```

### 2. Démarrer la base de données
```bash
cd backend
docker compose up -d
```
Attendre ~25 secondes que SQL Server soit prêt (healthcheck automatique).

### 3. Démarrer le backend
```bash
# Dans le dossier backend
.\mvnw clean spring-boot:run   # Windows
./mvnw clean spring-boot:run   # Linux/Mac
```
Flyway applique automatiquement les migrations V1 → V4 au premier démarrage.

### 4. Démarrer le frontend
```bash
cd frontend
npm install
npm start
```
Accéder à : http://localhost:4200

---

## Identifiants de test

### Membres
| Matricule | Mot de passe | Type | Site |
|-----------|-------------|------|------|
| G1042 | padel2026 | GLOBAL | Tous les sites |
| G1043 | padel2026 | GLOBAL | Tous les sites |
| G1044 | padel2026 | GLOBAL | Tous les sites |
| S2201 | padel2026 | SITE | Waterloo uniquement |
| S2202 | padel2026 | SITE | Waterloo (pénalité active) |
| S2401 | padel2026 | SITE | Louvain-la-Neuve uniquement |
| L3301 | padel2026 | LIBRE | Tous les sites (5j max) |
| L3302 | padel2026 | LIBRE | Tous les sites (5j max) |
| L3303 | padel2026 | LIBRE | Tous les sites (5j max) |

### Administrateurs
| Identifiant | Mot de passe | Type | Périmètre |
|-------------|-------------|------|-----------|
| ADMIN-1 | admin2026 | GLOBAL | Tous les sites |
| ADMIN-2 | admin2026 | SITE | Waterloo |
| ADMIN-3 | admin2026 | SITE | Louvain-la-Neuve |

---

## Règles métier démontrables

- **S2201** ne peut pas rejoindre un match sur un terrain LLN → "Terrain non autorisé"
- **S2202** a une pénalité active → ne peut ni créer ni rejoindre de match
- Un match PRIVÉ bascule en PUBLIC automatiquement si < 24h et < 4 participants (`@Scheduled` toutes les heures)
- L'organisateur d'un match peut l'annuler (supprime toutes les participations)
- Réservations uniquement sur créneaux à la demi-heure (00 ou 30 min)

---

## Structure du projet

```
padel-app-repo/
├── backend/
│   ├── src/main/java/be/ephec/padel_backend/
│   │   ├── controller/   # Couche Présentation (REST)
│   │   ├── service/      # Couche Métier
│   │   ├── repository/   # Couche Accès données (JPA)
│   │   ├── entity/       # Entités JPA
│   │   ├── dto/          # Objets de transfert
│   │   └── security/     # JWT + Spring Security
│   ├── src/main/resources/
│   │   └── db/migration/ # Scripts Flyway V1–V4
│   ├── init.sql          # Création DB + utilisateur padel_app
│   └── docker-compose.yml
└── frontend/
    └── src/app/features/ # Pages Angular (login, réservation, admin, membres)
```
