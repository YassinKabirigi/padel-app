import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ParticipationDetailModel {
  idParticipation: number;
  matriculeMembre: string;
  nomMembre: string;
  dateMatch: string;
  terrainNumero: string;
  siteNom: string;
  statutMatch: string;
  estOrganisateur: boolean;
  aPaye: boolean;
  montantPaye: string;
}

@Injectable({
  providedIn: 'root'
})
export class Participation {
  private apiUrl = 'http://localhost:8080/api/participations';

  constructor(private http: HttpClient) {}

  getAllParticipations(): Observable<ParticipationDetailModel[]> {
    return this.http.get<ParticipationDetailModel[]>(this.apiUrl);
  }
}
