import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Site, SiteModel } from '../../core/services/site';
import { Terrain, TerrainModel } from '../../core/services/terrain';

@Component({
  selector: 'app-admin',
  imports: [CommonModule, FormsModule],
  templateUrl: './admin.html',
  styleUrl: './admin.scss'
})
export class Admin implements OnInit {
  sites: SiteModel[] = [];
  terrains: TerrainModel[] = [];

  // Formulaire nouveau site
  nouveauSiteNom = '';
  nouveauSiteAdresse = '';
  nouveauSiteOuverture = '08:00';
  nouveauSiteFermeture = '22:00';

  // Formulaire nouveau terrain
  nouveauTerrainNumero = '';
  nouveauTerrainSiteId: number | null = null;

  erreur = '';
  succes = '';

  constructor(
    private siteService: Site,
    private terrainService: Terrain,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.chargerDonnees();
  }

  chargerDonnees(): void {
    this.siteService.getAllSites().subscribe({
      next: (data) => {
        this.sites = data;
        this.cdr.detectChanges();
      }
    });
    this.terrainService.getAllTerrains().subscribe({
      next: (data) => {
        this.terrains = data;
        this.cdr.detectChanges();
      }
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
          this.erreur = 'Erreur lors de la création du site';
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
          this.erreur = 'Erreur lors de la création du terrain';
          this.cdr.detectChanges();
        }
      });
  }
}
