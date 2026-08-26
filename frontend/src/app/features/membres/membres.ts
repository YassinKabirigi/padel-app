import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Site, SiteModel } from '../../core/services/site';
import { Membre, MembreModel } from '../../core/services/membre';
import { Administrateur, AdministrateurModel } from '../../core/services/administrateur';
import { Penalite, PenaliteModel } from '../../core/services/penalite';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatTabsModule } from '@angular/material/tabs';
import { Badge } from '../../shared/badge/badge';
import { MembreDialog } from './membre-dialog/membre-dialog';
import { AdministrateurDialog } from './administrateur-dialog/administrateur-dialog';

@Component({
  selector: 'app-membres',
  imports: [CommonModule, FormsModule, MatCardModule, MatButtonModule, MatDialogModule, MatIconModule, MatTabsModule, Badge],
  templateUrl: './membres.html',
  styleUrl: './membres.scss'
})
export class Membres implements OnInit {
  sites: SiteModel[] = [];
  membres: MembreModel[] = [];
  membreExpande: string | null = null;

  toggleProfil(matricule: string): void {
    this.membreExpande = this.membreExpande === matricule ? null : matricule;
  }
  administrateurs: AdministrateurModel[] = [];
  penalites: PenaliteModel[] = [];

  erreur = '';
  succes = '';

  constructor(
    private siteService: Site,
    private membreService: Membre,
    private administrateurService: Administrateur,
    private penaliteService: Penalite,
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
    setTimeout(() => { this.succes = ''; this.cdr.detectChanges(); }, 4000);
  }

  private afficherErreur(message: string): void {
    this.succes = '';
    this.erreur = message;
    this.cdr.detectChanges();
    setTimeout(() => { this.erreur = ''; this.cdr.detectChanges(); }, 5000);
  }

  chargerDonnees(): void {
    this.siteService.getAllSites().subscribe({
      next: (data) => { this.sites = data; this.cdr.detectChanges(); }
    });
    this.membreService.getAllMembres().subscribe({
      next: (data) => { this.membres = data; this.cdr.detectChanges(); }
    });
    this.administrateurService.getAllAdministrateurs().subscribe({
      next: (data) => { this.administrateurs = data; this.cdr.detectChanges(); }
    });
    this.penaliteService.getPenalitesActives().subscribe({
      next: (data) => { this.penalites = data; this.cdr.detectChanges(); }
    });
  }

  ouvrirDialogMembre(membre: MembreModel | null): void {
    const dialogRef = this.dialog.open(MembreDialog, {
      width: '450px',
      data: { membre, sites: this.sites }
    });

    dialogRef.afterClosed().subscribe((resultat) => {
      if (!resultat) {
        return;
      }

      if (membre) {
        this.membreService.updateMembre(membre.matricule, resultat).subscribe({
          next: () => {
            this.afficherSucces('Membre modifié avec succès');
            this.chargerDonnees();
          },
          error: (err) => {
            this.afficherErreur(err.error?.erreur || 'Erreur lors de la modification');
          }
        });
      } else {
        const prefixe = resultat.typeMembre === 'GLOBAL' ? 'G'
          : resultat.typeMembre === 'SITE' ? 'S' : 'L';
        const matricule = prefixe + Math.floor(1000 + Math.random() * 9000);

        const nouveauMembre: MembreModel = {
          matricule,
          nom: resultat.nom,
          prenom: resultat.prenom,
          email: resultat.email,
          telephone: resultat.telephone,
          dateInscription: new Date().toISOString().split('T')[0],
          typeMembre: resultat.typeMembre,
          site: resultat.site
        };

        this.membreService.createMembre(nouveauMembre).subscribe({
          next: () => {
            this.afficherSucces(`Membre créé avec succès (matricule : ${matricule})`);
            this.chargerDonnees();
          },
          error: () => {
            this.afficherErreur('Erreur lors de la création du membre');
          }
        });
      }
    });
  }

  supprimerMembre(matricule: string): void {
    if (!confirm('Confirmer la suppression de ce membre ?')) {
      return;
    }
    this.membreService.deleteMembre(matricule).subscribe({
      next: () => {
        this.afficherSucces('Membre supprimé');
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

  ouvrirDialogAdministrateur(admin: AdministrateurModel | null): void {
    const dialogRef = this.dialog.open(AdministrateurDialog, {
      width: '450px',
      data: { admin, sites: this.sites }
    });

    dialogRef.afterClosed().subscribe((resultat) => {
      if (!resultat) {
        return;
      }

      const operation = admin
        ? this.administrateurService.updateAdministrateur(admin.idAdmin, resultat)
        : this.administrateurService.createAdministrateur(resultat);

      operation.subscribe({
        next: (result) => {
          this.afficherSucces(
            admin
              ? 'Administrateur modifié avec succès'
              : `Administrateur créé — identifiant de connexion : ADMIN-${result.idAdmin}`
          );
          this.chargerDonnees();
        },
        error: () => {
          this.afficherErreur('Erreur lors de l\'opération (réservé aux administrateurs globaux)');
        }
      });
    });
  }

  supprimerAdministrateur(id: number): void {
    if (!confirm('Confirmer la suppression de cet administrateur ?')) {
      return;
    }
    this.administrateurService.deleteAdministrateur(id).subscribe({
      next: () => {
        this.afficherSucces('Administrateur supprimé');
        this.chargerDonnees();
      },
      error: (err) => {
        if (err.status === 409) {
          this.afficherErreur(err.error?.erreur || 'Impossible de supprimer : élément encore référencé');
        } else {
          this.afficherErreur('Erreur lors de la suppression (réservé aux administrateurs globaux)');
        }
        this.chargerDonnees();
      }
    });
  }

  leverPenalite(matricule: string): void {
    if (!confirm('Confirmer la levée de cette pénalité ?')) {
      return;
    }
    this.penaliteService.leverPenalite(matricule).subscribe({
      next: () => {
        this.afficherSucces('Pénalité levée avec succès');
        this.chargerDonnees();
      },
      error: () => {
        this.afficherErreur('Erreur lors de la levée de la pénalité');
      }
    });
  }
}
