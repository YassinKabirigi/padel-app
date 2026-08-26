import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

export interface LoginResponse {
  token: string;
  matricule: string;
  typeMembre: string;
}

@Injectable({
  providedIn: 'root'
})
export class Auth {
  private apiUrl = 'http://localhost:8080/api/auth';
  private currentToken: string | null = null;
  private currentTypeMembre: string | null = null;
  private currentMatricule: string | null = null;

  constructor(private http: HttpClient) {}

  login(matricule: string, motDePasse: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, { matricule, motDePasse }).pipe(
      tap((response) => {
        this.currentToken = response.token;
        this.currentTypeMembre = response.typeMembre;
        this.currentMatricule = response.matricule;
      })
    );
  }

  logout(): void {
    this.currentToken = null;
    this.currentTypeMembre = null;
    this.currentMatricule = null;
  }

  getToken(): string | null { return this.currentToken; }
  getTypeMembre(): string | null { return this.currentTypeMembre; }
  getMatricule(): string | null { return this.currentMatricule; }
  isAuthenticated(): boolean { return this.currentToken !== null; }
}
