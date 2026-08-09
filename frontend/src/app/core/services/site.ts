import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface SiteModel {
  idSite: number;
  nom: string;
  adresse: string;
  heureOuverture: string;
  heureFermeture: string;
}

@Injectable({
  providedIn: 'root'
})
export class Site {
  private apiUrl = 'http://localhost:8080/api/sites';

  constructor(private http: HttpClient) {}

  getAllSites(): Observable<SiteModel[]> {
    return this.http.get<SiteModel[]>(this.apiUrl);
  }
  createSite(site: Omit<SiteModel, 'idSite'>): Observable<SiteModel> {
    return this.http.post<SiteModel>(this.apiUrl, site);
  }
  updateSite(id: number, site: Omit<SiteModel, 'idSite'>): Observable<SiteModel> {
    return this.http.put<SiteModel>(`${this.apiUrl}/${id}`, site);
  }

  deleteSite(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
