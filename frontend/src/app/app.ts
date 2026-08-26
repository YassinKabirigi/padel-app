import { Component, signal } from '@angular/core';
import { Router, RouterOutlet, RouterLink } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Auth } from './core/services/auth';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, MatToolbarModule, MatButtonModule, MatIconModule],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('padel-frontend');

  constructor(public auth: Auth, private router: Router) {}

  estAdmin(): boolean {
    const type = this.auth.getTypeMembre();
    return !!type && type.startsWith('ADMIN_');
  }
  onLogout(): void {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
