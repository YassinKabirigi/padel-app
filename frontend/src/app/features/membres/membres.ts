import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Site, SiteModel } from '../../core/services/site';
import { Membre, MembreModel } from '../../core/services/membre';
import { Administrateur, AdministrateurModel } from '../../core/services/administrateur';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatChipsModule } from '@angular/material/chips';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MembreDialog } from './membre-dialog/membre-dialog';

@Component({
  selector: 'app-membres',
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatChipsModule,
    MatDialogModule,
    MatIconModule
  ],
  templateUrl: './membres.html',
  styleUrl: './membres.scss'
})
export class Membres implements OnInit {
  sites: SiteModel[] = [];
  membres: MembreModel[] = [];
  administrateurs: AdministrateurModel[] = [];

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
  }

  couleurBadge(type: string): string {
    switch (type) {
      case 'GLOBAL': return '#7c4dff';
      case 'SITE': return '#00bcd4';
      case 'LIBRE': return '#66bb6a';
      default: return '#888';
    }
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
        this.afficherErreur('Erreur lors de la création du membre');
      }
    });
  }

  ouvrirDialogModifierMembre(membre: MembreModel): void {
    const dialogRef = this.dialog.open(MembreDialog, {
      width: '450px',
      data: { membre, sites: this.sites }
    });

    dialogRef.afterClosed().subscribe((resultat) => {
      if (!resultat) {
        return;
      }

      this.membreService.updateMembre(membre.matricule, resultat).subscribe({
        next: () => {
          this.afficherSucces('Membre modifié avec succès');
          this.chargerDonnees();
        },
        error: (err) => {
          this.afficherErreur(err.error?.erreur || 'Erreur lors de la modification');
        }
      });
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

  // ----- Administrateurs -----

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
}
