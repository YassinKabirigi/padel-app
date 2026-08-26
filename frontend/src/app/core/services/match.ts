import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CreerMatchRequest {
  idTerrain: number;
  dateHeureDebut: string;
  statut: string;
  matriculeOrganisateur: string;
}

export interface MatchModel {
  idMatch: number;
  dateHeureDebut: string;
  statut: string;
}

export interface MatchDisponibleModel {
  idMatch: number;
  dateHeureDebut: string;
  terrainNumero: string;
  siteNom: string;
  statut: string;
  nbParticipants: number;
  dejaParticipant: boolean;
  peutRejoindre: boolean;
  estOrganisateur: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class Match {
  private apiUrl = 'http://localhost:8080/api/matches';

  constructor(private http: HttpClient) {}

  creerMatch(match: {
    idTerrain: number;
    dateHeureDebut: string;
    statut: string;
    matriculeOrganisateur: string;
    matriculesCoequipiers?: string[];
  }): Observable<any> {
    return this.http.post<any>(this.apiUrl, match);
  }

  getMatchsDisponibles(): Observable<MatchDisponibleModel[]> {
    return this.http.get<MatchDisponibleModel[]>(`${this.apiUrl}/disponibles`);
  }

  rejoindreMatch(idMatch: number): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/${idMatch}/rejoindre`, {});
  }

  annulerMatch(idMatch: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${idMatch}`);
  }
}
