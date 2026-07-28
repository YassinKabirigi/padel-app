import { Routes } from '@angular/router';
import { Login } from './features/login/login';
import { SitesTest } from './features/sites-test/sites-test';
import { authGuard } from './core/guards/auth-guard';

export const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'sites', component: SitesTest, canActivate: [authGuard] },
  { path: '', redirectTo: '/login', pathMatch: 'full' }
];
