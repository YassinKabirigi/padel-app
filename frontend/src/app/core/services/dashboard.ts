import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface MonProfilModel {
  matricule: string;
  nom: string;
  prenom: string;
  email: string;
  telephone: string;
  typeMembre: string;
  siteNom: string | null;
}

export interface MesPaiementsModel {
  montantDu: number;
  montantPaye: number;
  soldeRestant: number;
}

export interface MesStatistiquesModel {
  matchsJoues: number;
  reservationsAVenir: number;
  matchsPrives: number;
  matchsPublics: number;
}

@Injectable({
  providedIn: 'root'
})
export class Dashboard {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  getMonProfil(): Observable<MonProfilModel> {
    return this.http.get<MonProfilModel>(`${this.apiUrl}/membres/me`);
  }

  getMesPaiements(): Observable<MesPaiementsModel> {
    return this.http.get<MesPaiementsModel>(`${this.apiUrl}/membres/me/paiements`);
  }

  getMesStatistiques(): Observable<MesStatistiquesModel> {
    return this.http.get<MesStatistiquesModel>(`${this.apiUrl}/membres/me/stats`);
  }

  getMesReservations(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/participations/me`);
  }
}
