import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Site, SiteModel } from '../../core/services/site';
import { Terrain, TerrainModel } from '../../core/services/terrain';
import { Participation, ParticipationDetailModel } from '../../core/services/participation';
import { Membre } from '../../core/services/membre';
import { Administrateur } from '../../core/services/administrateur';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { Badge } from '../../shared/badge/badge';
import { SiteDialog } from './site-dialog/site-dialog';
import { TerrainDialog } from './terrain-dialog/terrain-dialog';

@Component({
  selector: 'app-admin',
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatDialogModule,
    MatIconModule,
    Badge
  ],
  templateUrl: './admin.html',
  styleUrl: './admin.scss'
})
export class Admin implements OnInit {
  sites: SiteModel[] = [];
  terrains: TerrainModel[] = [];
  participations: ParticipationDetailModel[] = [];
  nbMembres = 0;
  nbAdministrateurs = 0;

  erreur = '';
  succes = '';

  constructor(
    private siteService: Site,
    private terrainService: Terrain,
    private participationService: Participation,
    private membreService: Membre,
    private administrateurService: Administrateur,
    private dialog: MatDialog,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.chargerDonnees();
  }

  private afficherSucces(message: string): void {
    this.erreur = '';
    this.succes = message;
    this.cdr.detectChanges();
    setTimeout(() => {
      this.succes = '';
      this.cdr.detectChanges();
    }, 4000);
  }

  private afficherErreur(message: string): void {
    this.succes = '';
    this.erreur = message;
    this.cdr.detectChanges();
    setTimeout(() => {
      this.erreur = '';
      this.cdr.detectChanges();
    }, 5000);
  }

  chargerDonnees(): void {
    this.siteService.getAllSites().subscribe({
      next: (data) => { this.sites = data; this.cdr.detectChanges(); }
    });
    this.terrainService.getAllTerrains().subscribe({
      next: (data) => { this.terrains = data; this.cdr.detectChanges(); }
    });
    this.participationService.getAllParticipations().subscribe({
      next: (data) => { this.participations = data; this.cdr.detectChanges(); }
    });
    this.membreService.getAllMembres().subscribe({
      next: (data) => { this.nbMembres = data.length; this.cdr.detectChanges(); }
    });
    this.administrateurService.getAllAdministrateurs().subscribe({
      next: (data) => { this.nbAdministrateurs = data.length; this.cdr.detectChanges(); }
    });
  }

  ouvrirDialogSite(site: SiteModel | null): void {
    const dialogRef = this.dialog.open(SiteDialog, {
      width: '450px',
      data: { site }
    });

    dialogRef.afterClosed().subscribe((resultat) => {
      if (!resultat) {
        return;
      }

      const operation = site
        ? this.siteService.updateSite(site.idSite, resultat)
        : this.siteService.createSite(resultat);

      operation.subscribe({
        next: () => {
          this.afficherSucces(site ? 'Site modifié avec succès' : 'Site créé avec succès');
          this.chargerDonnees();
        },
        error: (err) => {
          this.afficherErreur(err.error?.erreur || 'Erreur lors de l\'opération');
        }
      });
    });
  }

  supprimerSite(id: number): void {
    if (!confirm('Confirmer la suppression de ce site ?')) {
      return;
    }
    this.siteService.deleteSite(id).subscribe({
      next: () => {
        this.afficherSucces('Site supprimé');
        this.chargerDonnees();
      },
      error: (err) => {
        if (err.status === 409) {
          this.afficherErreur(err.error?.erreur || 'Impossible de supprimer : élément encore référencé');
        } else if (err.status === 404) {
          this.afficherErreur('Ce site n\'existe plus (déjà supprimé ?)');
        } else if (err.status === 403) {
          this.afficherErreur('Droits insuffisants pour supprimer ce site');
        } else {
          this.afficherErreur('Erreur lors de la suppression');
        }
        this.chargerDonnees();
      }
    });
  }

  ouvrirDialogTerrain(terrain: TerrainModel | null): void {
    const dialogRef = this.dialog.open(TerrainDialog, {
      width: '450px',
      data: { terrain, sites: this.sites }
    });

    dialogRef.afterClosed().subscribe((resultat) => {
      if (!resultat) {
        return;
      }

      const operation = terrain
        ? this.terrainService.updateTerrain(terrain.idTerrain, resultat)
        : this.terrainService.createTerrain(resultat);

      operation.subscribe({
        next: () => {
          this.afficherSucces(terrain ? 'Terrain modifié avec succès' : 'Terrain créé avec succès');
          this.chargerDonnees();
        },
        error: (err) => {
          this.afficherErreur(err.error?.erreur || 'Erreur lors de l\'opération');
        }
      });
    });
  }

  supprimerTerrain(id: number): void {
    if (!confirm('Confirmer la suppression de ce terrain ?')) {
      return;
    }
    this.terrainService.deleteTerrain(id).subscribe({
      next: () => {
        this.afficherSucces('Terrain supprimé');
        this.chargerDonnees();
      },
      error: (err) => {
        if (err.status === 409) {
          this.afficherErreur(err.error?.erreur || 'Impossible de supprimer : élément encore référencé');
        } else {
          this.afficherErreur('Erreur lors de la suppression');
        }
        this.chargerDonnees();
      }
    });
  }
}
