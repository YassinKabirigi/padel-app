import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatButtonModule } from '@angular/material/button';
import { Badge } from '../../shared/badge/badge';
import {
  Dashboard as DashboardService,
  MonProfilModel,
  MesPaiementsModel,
  MesStatistiquesModel,
  HistoriquePaiementModel
} from '../../core/services/dashboard';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, MatCardModule, MatIconModule, MatChipsModule, MatButtonModule, Badge],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class DashboardComponent implements OnInit {
  profil: MonProfilModel | null = null;
  paiements: MesPaiementsModel | null = null;
  stats: MesStatistiquesModel | null = null;
  reservations: any[] = [];
  historiquePaiements: HistoriquePaiementModel[] = [];

  constructor(private dashboardService: DashboardService, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.chargerDonnees();
  }

  chargerDonnees(): void {
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
    this.dashboardService.getHistoriquePaiements().subscribe({
      next: (data) => { this.historiquePaiements = data; this.cdr.detectChanges(); }
    });
  }

  payer(idParticipation: number): void {
    this.dashboardService.payerParticipation(idParticipation).subscribe({
      next: () => {
        this.chargerDonnees();
      },
      error: () => {
        alert('Erreur lors du paiement');
      }
    });
  }
}
