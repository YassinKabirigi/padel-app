import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface TerrainModel {
  idTerrain: number;
  numero: string;
  site: {
    idSite: number;
    nom: string;
  };
}

@Injectable({
  providedIn: 'root'
})
export class Terrain {
  private apiUrl = 'http://localhost:8080/api/terrains';

  constructor(private http: HttpClient) {}

  getAllTerrains(): Observable<TerrainModel[]> {
    return this.http.get<TerrainModel[]>(this.apiUrl);
  }
}
