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

@Injectable({
  providedIn: 'root'
})
export class Match {
  private apiUrl = 'http://localhost:8080/api/matches';

  constructor(private http: HttpClient) {}

  creerMatch(request: CreerMatchRequest): Observable<MatchModel> {
    return this.http.post<MatchModel>(this.apiUrl, request);
  }
}
