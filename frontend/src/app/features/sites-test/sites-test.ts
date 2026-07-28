import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Site, SiteModel } from '../../core/services/site';

@Component({
  selector: 'app-sites-test',
  imports: [CommonModule],
  templateUrl: './sites-test.html',
  styleUrl: './sites-test.scss'
})
export class SitesTest implements OnInit {
  sites: SiteModel[] = [];
  erreur: string = '';

  constructor(private siteService: Site) {}

  ngOnInit(): void {
    this.siteService.getAllSites().subscribe({
      next: (data) => {
        this.sites = data;
      },
      error: (err) => {
        this.erreur = 'Erreur de connexion au backend : ' + err.message;
        console.error(err);
      }
    });
  }
}
