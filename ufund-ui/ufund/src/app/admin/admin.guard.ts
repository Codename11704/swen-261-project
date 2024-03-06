import { Injectable, inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';

@Injectable({
  providedIn: 'root'
})
class AdminGuard {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean {
    if(!this.authService.isLoggedIn()) {
      this.router.navigate(['/login']);
    } else if(!this.authService.isAdmin()) {
      this.router.navigate(['./cupboard']);
    }
    return true;
  }
}

export const adminGuard: CanActivateFn = (route, state) => {
  return inject(AdminGuard).canActivate();
};
