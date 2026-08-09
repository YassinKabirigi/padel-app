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
  createTerrain(terrain: { numero: string; site: { idSite: number } }): Observable<TerrainModel> {
    return this.http.post<TerrainModel>(this.apiUrl, terrain);
  }

  deleteTerrain(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  updateTerrain(id: number, terrain: { numero: string; site: { idSite: number } }): Observable<TerrainModel> {
    return this.http.put<TerrainModel>(`${this.apiUrl}/${id}`, terrain);
  }
}
