import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import {
  Dashboard as DashboardService,
  MonProfilModel,
  MesPaiementsModel,
  MesStatistiquesModel
} from '../../core/services/dashboard';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, MatCardModule, MatIconModule, MatChipsModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class DashboardComponent implements OnInit {
  profil: MonProfilModel | null = null;
  paiements: MesPaiementsModel | null = null;
  stats: MesStatistiquesModel | null = null;
  reservations: any[] = [];

  constructor(private dashboardService: DashboardService, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.dashboardService.getMonProfil().subscribe({
      next: (data) => { this.profil = data; this.cdr.detectChanges(); }
    });
    this.dashboardService.getMesPaiements().subscribe({
      next: (data) => { this.paiements = data; this.cdr.detectChanges(); }
    });
    this.dashboardService.getMesStatistiques().subscribe({
      next: (data) => { this.stats = data; this.cdr.detectChanges(); }
    });
    this.dashboardService.getMesReservations().subscribe({
      next: (data) => { this.reservations = data; this.cdr.detectChanges(); }
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
}
