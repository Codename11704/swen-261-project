import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms'; 
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NeedsComponent } from './needs/needs.component';
import { HttpClientModule } from '@angular/common/http';
import { LoginComponent } from './login/login.component';
import { CheckoutComponent } from './needs/checkout/checkout.component';
import { NeedManagerComponent } from './admin/need-manager/need-manager.component';
import { RegisterComponent } from './register/register.component';
import { AdminModule } from './admin/admin.module';
import { UserSettingsComponent } from './user/user-settings/user-settings.component';

@NgModule({
  declarations: [
    AppComponent,
    NeedsComponent,
    LoginComponent,
    CheckoutComponent,
    NeedManagerComponent,
    RegisterComponent,
    UserSettingsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    AdminModule,
    HttpClientModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
