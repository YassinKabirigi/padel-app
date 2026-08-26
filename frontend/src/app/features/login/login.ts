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
  motDePasse: string = '';
  erreur: string = '';
  chargement: boolean = false;

  constructor(private authService: Auth, private router: Router) {}

  onSubmit(): void {
    if (!this.matricule.trim() || !this.motDePasse.trim()) {
      this.erreur = 'Veuillez entrer votre matricule et votre mot de passe';
      return;
    }

    this.chargement = true;
    this.erreur = '';

    this.authService.login(this.matricule.trim(), this.motDePasse).subscribe({
      next: () => {
        this.chargement = false;
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.chargement = false;
        if (err.status === 401) {
          this.erreur = 'Identifiant ou mot de passe incorrect';
        } else {
          this.erreur = 'Erreur de connexion au serveur';
        }
      }
    });
  }
}
