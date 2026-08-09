import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { Auth } from '../../core/services/auth';

@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule, MatCardModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  templateUrl: './login.html',
  styleUrl: './login.scss'
})
export class Login {
  matricule: string = '';
  erreur: string = '';
  chargement: boolean = false;

  constructor(private authService: Auth, private router: Router) {}

  onSubmit(): void {
    if (!this.matricule.trim()) {
      this.erreur = 'Veuillez entrer un matricule';
      return;
    }

    this.chargement = true;
    this.erreur = '';

    this.authService.login(this.matricule).subscribe({
      next: () => {
        this.chargement = false;
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.chargement = false;
        if (err.status === 401) {
          this.erreur = 'Matricule inconnu';
        } else {
          this.erreur = 'Erreur de connexion au serveur';
        }
      }
    });
  }
}
