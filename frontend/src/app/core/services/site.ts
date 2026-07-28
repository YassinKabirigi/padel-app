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
}
