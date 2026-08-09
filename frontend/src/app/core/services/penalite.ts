import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface PenaliteModel {
  matricule: string;
  nom: string;
  prenom: string;
  dateDebutPenalite: string;
  dateFinPenalite: string;
  motifPenalite: string;
}

@Injectable({
  providedIn: 'root'
})
export class Penalite {
  private apiUrl = 'http://localhost:8080/api/penalites';

  constructor(private http: HttpClient) {}

  getPenalitesActives(): Observable<PenaliteModel[]> {
    return this.http.get<PenaliteModel[]>(this.apiUrl);
  }

  leverPenalite(matricule: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${matricule}`);
  }
}
