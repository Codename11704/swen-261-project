import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { NeedsComponent } from './needs/needs.component';
import { authGuard } from './auth/auth.guard';
import { CheckoutComponent } from './needs/checkout/checkout.component';
import { NeedManagerComponent } from './admin/need-manager/need-manager.component';
import { adminGuard } from './admin/admin.guard';
import { RegisterComponent } from './register/register.component';
import { UserSettingsComponent } from './user/user-settings/user-settings.component';

const appRoutes: Routes = [
  { path: 'login', component: LoginComponent },
  {path: 'register', component: RegisterComponent},
  { path: 'cupboard', component: NeedsComponent, canActivate: [authGuard] },
  { path: 'checkout', component: CheckoutComponent, canActivate: [authGuard] },
  { path: 'admin', component: NeedManagerComponent, canActivate: [adminGuard] },
  { path: 'user', component: UserSettingsComponent, canActivate: [authGuard]},
  { path: '',   redirectTo: '/login', pathMatch: 'full' }


];

@NgModule({
  imports: [RouterModule.forRoot(appRoutes)],
  exports: [RouterModule]
})


export class AppRoutingModule { }
