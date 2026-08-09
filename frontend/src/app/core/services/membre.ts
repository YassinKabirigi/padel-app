import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface MembreModel {
  matricule: string;
  nom: string;
  prenom: string;
  email: string;
  telephone: string;
  dateInscription: string;
  typeMembre: string;
  site?: { idSite: number } | null;
}

@Injectable({
  providedIn: 'root'
})
export class Membre {
  private apiUrl = 'http://localhost:8080/api/membres';

  constructor(private http: HttpClient) {}

  getAllMembres(): Observable<MembreModel[]> {
    return this.http.get<MembreModel[]>(this.apiUrl);
  }

  createMembre(membre: MembreModel): Observable<MembreModel> {
    return this.http.post<MembreModel>(this.apiUrl, membre);
  }

  updateMembre(matricule: string, membre: Partial<MembreModel>): Observable<MembreModel> {
    return this.http.put<MembreModel>(`${this.apiUrl}/${matricule}`, membre);
  }

  deleteMembre(matricule: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${matricule}`);
  }
}
