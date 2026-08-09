import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Site, SiteModel } from '../../core/services/site';
import { Terrain, TerrainModel } from '../../core/services/terrain';
import { Membre, MembreModel } from '../../core/services/membre';
import { Participation, ParticipationDetailModel } from '../../core/services/participation';
import { Administrateur, AdministrateurModel } from '../../core/services/administrateur';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { SiteDialog } from './site-dialog/site-dialog';

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
    MatIconModule
  ],
  templateUrl: './admin.html',
  styleUrl: './admin.scss'
})
export class Admin implements OnInit {
  sites: SiteModel[] = [];
  terrains: TerrainModel[] = [];
  membres: MembreModel[] = [];
  participations: ParticipationDetailModel[] = [];
  administrateurs: AdministrateurModel[] = [];

  nouveauTerrainNumero = '';
  nouveauTerrainSiteId: number | null = null;

  nouveauMembreNom = '';
  nouveauMembrePrenom = '';
  nouveauMembreEmail = '';
  nouveauMembreTelephone = '';
  nouveauMembreType = 'GLOBAL';
  nouveauMembreSiteId: number | null = null;

  nouveauAdminNom = '';
  nouveauAdminPrenom = '';
  nouveauAdminEmail = '';
  nouveauAdminType = 'GLOBAL';
  nouveauAdminSiteId: number | null = null;

  erreur = '';
  succes = '';

  constructor(
    private siteService: Site,
    private terrainService: Terrain,
    private membreService: Membre,
    private participationService: Participation,
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
    this.membreService.getAllMembres().subscribe({
      next: (data) => { this.membres = data; this.cdr.detectChanges(); }
    });
    this.participationService.getAllParticipations().subscribe({
      next: (data) => { this.participations = data; this.cdr.detectChanges(); }
    });
    this.administrateurService.getAllAdministrateurs().subscribe({
      next: (data) => { this.administrateurs = data; this.cdr.detectChanges(); }
    });
  }
// ----- création Admin -----

  creerAdministrateur(): void {
    if (!this.nouveauAdminNom || !this.nouveauAdminPrenom || !this.nouveauAdminEmail) {
      this.afficherErreur('Veuillez remplir tous les champs obligatoires');
      return;
    }

    if (this.nouveauAdminType === 'SITE' && !this.nouveauAdminSiteId) {
      this.afficherErreur('Veuillez sélectionner un site pour un administrateur de type Site');
      return;
    }

    const nouvelAdmin: Omit<AdministrateurModel, 'idAdmin'> = {
      nom: this.nouveauAdminNom,
      prenom: this.nouveauAdminPrenom,
      email: this.nouveauAdminEmail,
      typeAdmin: this.nouveauAdminType,
      site: this.nouveauAdminType === 'SITE' ? { idSite: this.nouveauAdminSiteId! } : null
    };

    this.administrateurService.createAdministrateur(nouvelAdmin).subscribe({
      next: (admin) => {
        this.afficherSucces(`Administrateur créé — identifiant de connexion : ADMIN-${admin.idAdmin}`);
        this.nouveauAdminNom = '';
        this.nouveauAdminPrenom = '';
        this.nouveauAdminEmail = '';
        this.chargerDonnees();
      },
      error: () => {
        this.afficherErreur('Erreur lors de la création (réservé aux administrateurs globaux)');
      }
    });
  }

  // ----- Sites -----

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

  // ----- Terrains -----

  creerTerrain(): void {
    if (!this.nouveauTerrainNumero || !this.nouveauTerrainSiteId) {
      this.afficherErreur('Veuillez remplir le numéro et sélectionner un site');
      return;
    }

    this.terrainService
      .createTerrain({
        numero: this.nouveauTerrainNumero,
        site: { idSite: this.nouveauTerrainSiteId }
      })
      .subscribe({
        next: () => {
          this.afficherSucces('Terrain créé avec succès');
          this.nouveauTerrainNumero = '';
          this.chargerDonnees();
        },
        error: () => {
          this.afficherErreur('Erreur lors de la création du terrain (droits insuffisants ?)');
        }
      });
  }

  // ----- Membres -----

  creerMembre(): void {
    if (!this.nouveauMembreNom || !this.nouveauMembrePrenom || !this.nouveauMembreEmail) {
      this.afficherErreur('Veuillez remplir tous les champs obligatoires');
      return;
    }

    if (this.nouveauMembreType === 'SITE' && !this.nouveauMembreSiteId) {
      this.afficherErreur('Veuillez sélectionner un site pour un membre de type Site');
      return;
    }

    const prefixe = this.nouveauMembreType === 'GLOBAL' ? 'G'
      : this.nouveauMembreType === 'SITE' ? 'S' : 'L';
    const matricule = prefixe + Math.floor(1000 + Math.random() * 9000);

    const nouveauMembre: MembreModel = {
      matricule,
      nom: this.nouveauMembreNom,
      prenom: this.nouveauMembrePrenom,
      email: this.nouveauMembreEmail,
      telephone: this.nouveauMembreTelephone,
      dateInscription: new Date().toISOString().split('T')[0],
      typeMembre: this.nouveauMembreType,
      site: this.nouveauMembreType === 'SITE' ? { idSite: this.nouveauMembreSiteId! } : null
    };

    this.membreService.createMembre(nouveauMembre).subscribe({
      next: () => {
        this.afficherSucces(`Membre créé avec succès (matricule : ${matricule})`);
        this.nouveauMembreNom = '';
        this.nouveauMembrePrenom = '';
        this.nouveauMembreEmail = '';
        this.nouveauMembreTelephone = '';
        this.chargerDonnees();
      },
      error: () => {
        this.afficherErreur('Erreur lors de la création du membre (droits insuffisants ?)');
      }
    });
  }
}
