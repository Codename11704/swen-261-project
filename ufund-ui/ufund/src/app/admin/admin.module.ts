import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminRoutingModule } from './admin-routing.module';
import { RouterModule, Routes } from '@angular/router';
import { NeedManagerComponent } from './need-manager/need-manager.component';
import { adminGuard } from './admin.guard';

const adminRoutes: Routes = [
{path: 'admin', component: NeedManagerComponent, canActivate: [adminGuard]}
]
@NgModule({
  declarations: [],
  imports: [
    RouterModule.forRoot(adminRoutes),
    CommonModule,
    AdminRoutingModule
  ]
})
export class AdminModule { }
