import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Site, SiteModel } from '../../core/services/site';
import { Terrain, TerrainModel } from '../../core/services/terrain';
import { Membre, MembreModel } from '../../core/services/membre';
import { Participation, ParticipationDetailModel } from '../../core/services/participation';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-admin',
  imports: [CommonModule, FormsModule, MatCardModule, MatFormFieldModule, MatInputModule, MatSelectModule, MatButtonModule],
  templateUrl: './admin.html',
  styleUrl: './admin.scss'
})
export class Admin implements OnInit {
  sites: SiteModel[] = [];
  terrains: TerrainModel[] = [];
  membres: MembreModel[] = [];
  participations: ParticipationDetailModel[] = [];

  nouveauSiteNom = '';
  nouveauSiteAdresse = '';
  nouveauSiteOuverture = '08:00';
  nouveauSiteFermeture = '22:00';

  nouveauTerrainNumero = '';
  nouveauTerrainSiteId: number | null = null;

  nouveauMembreNom = '';
  nouveauMembrePrenom = '';
  nouveauMembreEmail = '';
  nouveauMembreTelephone = '';
  nouveauMembreType = 'GLOBAL';
  nouveauMembreSiteId: number | null = null;

  erreur = '';
  succes = '';

  constructor(
    private siteService: Site,
    private terrainService: Terrain,
    private membreService: Membre,
    private participationService: Participation,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.chargerDonnees();
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
  }

  creerSite(): void {
    this.erreur = '';
    this.succes = '';

    if (!this.nouveauSiteNom || !this.nouveauSiteAdresse) {
      this.erreur = 'Veuillez remplir le nom et l\'adresse du site';
      this.cdr.detectChanges();
      return;
    }

    this.siteService
      .createSite({
        nom: this.nouveauSiteNom,
        adresse: this.nouveauSiteAdresse,
        heureOuverture: this.nouveauSiteOuverture,
        heureFermeture: this.nouveauSiteFermeture
      })
      .subscribe({
        next: () => {
          this.succes = 'Site créé avec succès';
          this.nouveauSiteNom = '';
          this.nouveauSiteAdresse = '';
          this.chargerDonnees();
          this.cdr.detectChanges();
        },
        error: () => {
          this.erreur = 'Erreur lors de la création du site (droits insuffisants ?)';
          this.cdr.detectChanges();
        }
      });
  }

  creerTerrain(): void {
    this.erreur = '';
    this.succes = '';

    if (!this.nouveauTerrainNumero || !this.nouveauTerrainSiteId) {
      this.erreur = 'Veuillez remplir le numéro et sélectionner un site';
      this.cdr.detectChanges();
      return;
    }

    this.terrainService
      .createTerrain({
        numero: this.nouveauTerrainNumero,
        site: { idSite: this.nouveauTerrainSiteId }
      })
      .subscribe({
        next: () => {
          this.succes = 'Terrain créé avec succès';
          this.nouveauTerrainNumero = '';
          this.chargerDonnees();
          this.cdr.detectChanges();
        },
        error: () => {
          this.erreur = 'Erreur lors de la création du terrain (droits insuffisants ?)';
          this.cdr.detectChanges();
        }
      });
  }

  creerMembre(): void {
    this.erreur = '';
    this.succes = '';

    if (!this.nouveauMembreNom || !this.nouveauMembrePrenom || !this.nouveauMembreEmail) {
      this.erreur = 'Veuillez remplir tous les champs obligatoires';
      this.cdr.detectChanges();
      return;
    }

    if (this.nouveauMembreType === 'SITE' && !this.nouveauMembreSiteId) {
      this.erreur = 'Veuillez sélectionner un site pour un membre de type Site';
      this.cdr.detectChanges();
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
        this.succes = `Membre créé avec succès (matricule : ${matricule})`;
        this.nouveauMembreNom = '';
        this.nouveauMembrePrenom = '';
        this.nouveauMembreEmail = '';
        this.nouveauMembreTelephone = '';
        this.chargerDonnees();
        this.cdr.detectChanges();
      },
      error: () => {
        this.erreur = 'Erreur lors de la création du membre (droits insuffisants ?)';
        this.cdr.detectChanges();
      }
    });
  }
}
