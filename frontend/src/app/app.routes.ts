import { Routes } from '@angular/router';
import { Login } from './features/login/login';
import { Reservation } from './features/reservation/reservation';
import { authGuard } from './core/guards/auth-guard';

export const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'reservation', component: Reservation, canActivate: [authGuard] },
  { path: '', redirectTo: '/login', pathMatch: 'full' }
];
