import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Terrain, TerrainModel } from '../../core/services/terrain';
import { Match } from '../../core/services/match';
import { Auth } from '../../core/services/auth';

@Component({
  selector: 'app-reservation',
  imports: [CommonModule, FormsModule],
  templateUrl: './reservation.html',
  styleUrl: './reservation.scss'
})
export class Reservation implements OnInit {
  terrains: TerrainModel[] = [];

  idTerrainSelectionne: number | null = null;
  date: string = '';
  heure: string = '';
  statut: string = 'PRIVE';

  erreur: string = '';
  succes: string = '';
  chargement: boolean = false;

  constructor(
    private terrainService: Terrain,
    private matchService: Match,
    private authService: Auth,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.terrainService.getAllTerrains().subscribe({
      next: (data) => {
        this.terrains = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.erreur = 'Erreur de chargement des terrains';
        this.cdr.detectChanges();
      }
    });
  }

  onSubmit(): void {
    this.erreur = '';
    this.succes = '';

    if (!this.idTerrainSelectionne || !this.date || !this.heure) {
      this.erreur = 'Veuillez remplir tous les champs';
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
        matriculeOrganisateur: matricule
      })
      .subscribe({
        next: () => {
          this.chargement = false;
          this.succes = 'Match créé avec succès !';
          this.cdr.detectChanges();
        },
        error: (err) => {
          this.chargement = false;
          if (err.status === 400) {
            this.erreur = err.error || 'Réservation refusée';
          } else {
            this.erreur = 'Erreur serveur';
          }
          this.cdr.detectChanges();
        }
      });
  }
}
