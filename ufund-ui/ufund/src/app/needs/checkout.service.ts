import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Need } from '../objects/need';
import { catchError, subscribeOn } from 'rxjs';
import { Observable, of } from 'rxjs';
import { AuthService } from '../auth/auth.service';
import { NeedService } from '../needService/need.service';

@Injectable({
  providedIn: 'root'
})
export class CheckoutService {

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };
  
  constructor(private authService: AuthService, private needService: NeedService) {
  
  }

  public getPrice(need: Need): number | undefined {
    return this.authService.getCart().get(need.id.toString());
  }

  public addToCart(need: Need, amount: number) {
    this.authService.addToCart(need, amount);
    
  }

  public removeFromCart(need: Need) {
    this.authService.removeFromCart(need);
  }

  public checkout(): Observable<Need[]> {
    return this.needService.checkout(this.authService.getCart()!);

  }

}
