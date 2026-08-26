import { Routes } from '@angular/router';
import { Login } from './features/login/login';
import { Reservation } from './features/reservation/reservation';
import { Admin } from './features/admin/admin';
import { Membres } from './features/membres/membres';
import { DashboardComponent } from './features/dashboard/dashboard';
import { authGuard } from './core/guards/auth-guard';
import { adminGuard } from './core/guards/admin-guard';

export const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] },
  { path: 'reservation', component: Reservation, canActivate: [authGuard] },
  { path: 'admin', component: Admin, canActivate: [authGuard, adminGuard] },
  { path: 'membres', component: Membres, canActivate: [authGuard, adminGuard] },
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' }
];
