import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface JourFermetureModel {
  idFermeture: number;
  dateFermeture: string;
  motif: string;
  idSite: number | null;
  siteNom: string | null;
}

@Injectable({
  providedIn: 'root'
})
export class JourFermeture {
  private apiUrl = 'http://localhost:8080/api/jours-fermeture';

  constructor(private http: HttpClient) {}

  getAllFermetures(): Observable<JourFermetureModel[]> {
    return this.http.get<JourFermetureModel[]>(this.apiUrl);
  }

  createFermeture(fermeture: { dateFermeture: string; motif: string; idSite: number | null }): Observable<JourFermetureModel> {
    return this.http.post<JourFermetureModel>(this.apiUrl, fermeture);
  }

  updateFermeture(id: number, fermeture: { dateFermeture: string; motif: string; idSite: number | null }): Observable<JourFermetureModel> {
    return this.http.put<JourFermetureModel>(`${this.apiUrl}/${id}`, fermeture);
  }

  deleteFermeture(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
