import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface AdministrateurModel {
  idAdmin: number;
  nom: string;
  prenom: string;
  email: string;
  typeAdmin: string;
  site?: { idSite: number } | null;
}

@Injectable({
  providedIn: 'root'
})
export class Administrateur {
  private apiUrl = 'http://localhost:8080/api/administrateurs';

  constructor(private http: HttpClient) {}

  getAllAdministrateurs(): Observable<AdministrateurModel[]> {
    return this.http.get<AdministrateurModel[]>(this.apiUrl);
  }

  createAdministrateur(admin: Omit<AdministrateurModel, 'idAdmin'>): Observable<AdministrateurModel> {
    return this.http.post<AdministrateurModel>(this.apiUrl, admin);
  }
}
