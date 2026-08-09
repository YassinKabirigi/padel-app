import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { Auth } from '../services/auth';

export const adminGuard: CanActivateFn = (route, state) => {
  const authService = inject(Auth);
  const router = inject(Router);

  const type = authService.getTypeMembre();

  if (type && type.startsWith('ADMIN_')) {
    return true;
  }

  router.navigate(['/dashboard']);
  return false;
};
