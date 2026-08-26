import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Terrain, TerrainModel } from '../../core/services/terrain';
import { Match, MatchDisponibleModel } from '../../core/services/match';
import { Auth } from '../../core/services/auth';
import { Membre } from '../../core/services/membre';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Badge } from '../../shared/badge/badge';

@Component({
  selector: 'app-reservation',
  imports: [CommonModule, FormsModule, MatCardModule, MatFormFieldModule, MatInputModule, MatSelectModule, MatButtonModule, MatIconModule, Badge],
  templateUrl: './reservation.html',
  styleUrl: './reservation.scss'
})
export class Reservation implements OnInit {
  terrains: TerrainModel[] = [];
  matchsDisponibles: MatchDisponibleModel[] = [];
  filtreStatut: string = 'TOUS';

  get aujourdhui(): string {
    return new Date().toISOString().split('T')[0];
  }

  get matchsFiltres(): MatchDisponibleModel[] {
    if (this.filtreStatut === 'TOUS') return this.matchsDisponibles;
    return this.matchsDisponibles.filter(m => m.statut === this.filtreStatut);
  }

  idTerrainSelectionne: number | null = null;
  date: string = '';
  heure: string = '';
  statut: string = 'PRIVE';
  coequipiers: string[] = [];
  nouveauCoequipier: string = '';

  erreur: string = '';
  succes: string = '';
  chargement: boolean = false;
  verificationCoequipier: boolean = false;

  constructor(
    private terrainService: Terrain,
    private matchService: Match,
    private authService: Auth,
    private membreService: Membre,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.chargerDonnees();
  }

  chargerDonnees(): void {
    this.terrainService.getAllTerrains().subscribe({
      next: (data) => {
        this.terrains = data;
        this.cdr.detectChanges();
      },
      error: () => {
        this.erreur = 'Erreur de chargement des terrains';
        this.cdr.detectChanges();
      }
    });

    this.matchService.getMatchsDisponibles().subscribe({
      next: (data) => {
        this.matchsDisponibles = data;
        this.cdr.detectChanges();
      }
    });
  }

  ajouterCoequipier(): void {
    const matricule = this.nouveauCoequipier.trim().toUpperCase();
    if (!matricule) {
      this.erreur = 'Veuillez saisir un matricule';
      this.cdr.detectChanges();
      return;
    }
    // Item 11 : validation format matricule (G/S/L + 4 chiffres)
    if (!/^[GSL]\d{4}$/.test(matricule)) {
      this.erreur = 'Format invalide — matricule : G/S/L suivi de 4 chiffres (ex: G1042)';
      this.cdr.detectChanges();
      return;
    }
    if (this.coequipiers.includes(matricule)) {
      this.erreur = 'Ce matricule a déjà été ajouté';
      this.cdr.detectChanges();
      return;
    }
    if (this.coequipiers.length >= 3) {
      this.erreur = 'Maximum 3 coéquipiers (4 joueurs au total avec vous)';
      this.cdr.detectChanges();
      return;
    }
    // Item 12 : vérification existence du membre avant ajout
    this.verificationCoequipier = true;
    this.erreur = '';
    this.cdr.detectChanges();
    this.membreService.getMembreByMatricule(matricule).subscribe({
      next: (membre) => {
        this.coequipiers.push(matricule);
        this.nouveauCoequipier = '';
        this.succes = `✓ ${membre.prenom} ${membre.nom} ajouté`;
        this.verificationCoequipier = false;
        this.cdr.detectChanges();
        setTimeout(() => { this.succes = ''; this.cdr.detectChanges(); }, 3000);
      },
      error: (err) => {
        this.verificationCoequipier = false;
        if (err.status === 404) {
          this.erreur = `Membre introuvable : ${matricule}`;
        } else {
          this.erreur = 'Erreur lors de la vérification du membre';
        }
        this.cdr.detectChanges();
      }
    });
  }

  retirerCoequipier(matricule: string): void {
    this.coequipiers = this.coequipiers.filter(m => m !== matricule);
  }

  onSubmit(): void {
    this.erreur = '';
    this.succes = '';

    if (!this.idTerrainSelectionne) {
      this.erreur = 'Veuillez sélectionner un terrain';
      return;
    }
    if (!this.date) {
      this.erreur = 'Veuillez saisir une date';
      return;
    }
    if (!this.heure) {
      this.erreur = 'Veuillez saisir une heure';
      return;
    }
    // Item 11 : vérifier que la date n'est pas dans le passé
    const dateChoisie = new Date(this.date + 'T' + this.heure);
    if (dateChoisie <= new Date()) {
      this.erreur = 'La date et l\'heure doivent être dans le futur';
      return;
    }
    // Item 10 : vérifier que l'heure est un créneau de 30 min (:00 ou :30)
    const minutes = dateChoisie.getMinutes();
    if (minutes !== 0 && minutes !== 30) {
      this.erreur = 'L\'heure doit être un créneau de 30 minutes (ex: 09:00 ou 09:30)';
      return;
    }

    const matricule = this.authService.getMatricule();
    if (!matricule) {
      this.erreur = 'Vous devez être connecté';
      return;
    }

    const dateHeureDebut = `${this.date}T${this.heure}:00`;

    this.chargement = true;

    this.matchService
      .creerMatch({
        idTerrain: this.idTerrainSelectionne,
        dateHeureDebut,
        statut: this.statut,
        matriculeOrganisateur: matricule,
        matriculesCoequipiers: this.coequipiers.length > 0 ? this.coequipiers : undefined
      })
      .subscribe({
        next: () => {
          this.chargement = false;
          this.succes = 'Match créé avec succès !';
          this.coequipiers = [];
          this.chargerDonnees();
          this.cdr.detectChanges();
        },
        error: (err) => {
          this.chargement = false;
          if (err.status === 400) {
            this.erreur = err.error?.erreur || 'Réservation refusée';
          } else {
            this.erreur = 'Erreur serveur';
          }
          this.cdr.detectChanges();
        }
      });
  }

  rejoindre(idMatch: number): void {
    this.erreur = '';
    this.succes = '';

    this.matchService.rejoindreMatch(idMatch).subscribe({
      next: () => {
        this.succes = 'Vous avez rejoint le match avec succès !';
        this.chargerDonnees();
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.erreur = err.error?.erreur || 'Impossible de rejoindre ce match';
        this.cdr.detectChanges();
      }
    });
  }

  annuler(idMatch: number): void {
    if (!confirm("Confirmer l'annulation de ce match ? Tous les participants seront retires.")) {
      return;
    }
    this.erreur = '';
    this.succes = '';

    this.matchService.annulerMatch(idMatch).subscribe({
      next: () => {
        this.succes = 'Match annule avec succes.';
        this.chargerDonnees();
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        this.erreur = err.error?.erreur || "Impossible d'annuler ce match";
        this.cdr.detectChanges();
      }
    });
  }
}
