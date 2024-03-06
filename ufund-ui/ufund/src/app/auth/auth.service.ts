import { Injectable } from '@angular/core';
import { LoginService } from '../auth/login.service';
import { User } from '../objects/user';
import { Router } from '@angular/router';
import { Need } from '../objects/need';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private currentUser?: string;
  private cart?: Map<string, number>;

  constructor(private loginService: LoginService, private router: Router) {

  }

  login(username: string, password: string): boolean {
    let response = false;
    this.loginService.login(username, password).subscribe(
      (value) => {
      if(value) {
        this.currentUser = username;
      }
      this.router.navigate(['admin']);
      response = value
    });
    return response;
  }

  doSomething() {

  }

  createAccount(username: string, password: string): boolean {
    let response = false;
    this.loginService.createAccount({user: username, pass: password, checkout: new Map<number, number>}).subscribe(
      (value) => {
      if(value) {
        this.currentUser = username;
      }
      this.router.navigate(['admin']);
      response = value;
    });
    return response;
  }

  public logout() {
    this.currentUser = undefined;
    this.cart = undefined;
    this.router.navigate(['/login']);
  }

  public isLoggedIn(): boolean {
    if(this.currentUser) {
      return true;
    }
    return false;
  }

  public isAdmin(): boolean {
    if(!this.currentUser) return false;
    if(this.currentUser === "admin") {
      console.log('Is Admin');
      return true;
    }
    this.getUserCart();
    return false;
  }

  public getCurrentUser(): string | undefined{
    return this.currentUser;
  }

  public addToCart(need: Need, amount: number) {
    this.loginService.addtoCart(this.currentUser!, need, amount).subscribe(() => {
      this.getUserCart();
    });
  }

  public removeFromCart(need: Need) {
    this.loginService.removeFromCart(this.currentUser!, need.id).subscribe(() => {
      this.getUserCart();
    });
  }

  public getUserCart() {
    this.loginService.getCart(this.currentUser!).subscribe((map) => {
      this.cart = new Map(Object.entries(map));
    });
  }

  public getCart(): Map<string, number> {
    return this.cart!;
  }

  public updateUser(username: string): boolean{
    let response = false
    this.loginService.updateUsername(this.currentUser!, username).subscribe(
      (value) => {
          if(value) {
            this.currentUser = username;
          }
          this.router.navigate(['admin']);
          response = value;
      });
    return response
  }

  public updatePass(pass: string){
    this.loginService.updatePassword(this.currentUser!, pass).subscribe()
  }

  public deleteAccount() {
    this.loginService.deleteAccount(this.currentUser!).subscribe();
  }
}
