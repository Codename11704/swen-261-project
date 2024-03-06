import { Component } from '@angular/core';
import { CheckoutService } from '../checkout.service';
import { Router } from '@angular/router';
import { Need } from 'src/app/objects/need';
import { AuthService } from 'src/app/auth/auth.service';
import { NeedService } from 'src/app/needService/need.service';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent {

  constructor(private checkoutService: CheckoutService, private authService: AuthService, private needService: NeedService, private router: Router) {}

  needs: Need[] = [];
  total: number = 0;

  ngOnInit(): void {
    this.getNeeds();
  }

  private getNeeds() {
    this.needService.getNeeds().subscribe((needs) => {
      needs.forEach((need) => {
        
        if(this.authService.getCart().has(need.id.toString())) {
          this.needs.push(need);
          this.total += this.getPrice(need)!;
        }
      })
    })
  }

  getPrice(need: Need): number | null | undefined {
    let price = this.checkoutService.getPrice(need);
    //console.log(price);
    return price;
  }

  purchase() {
    this.checkoutService.checkout().subscribe();
    this.needs.forEach((need) => {
      this.checkoutService.removeFromCart(need);
    });
    this.needs = [];
    this.router.navigate(['/cupboard']);
  }

  remove(need: Need) {
    let ind = this.needs.indexOf(need);
    this.needs.splice(ind, 1);
    this.total -= this.getPrice(need)!;
    this.checkoutService.removeFromCart(need);
  }

  logout() {
    this.authService.logout();
  }

  back() {
    this.router.navigate(['/cupboard'])
  }
}
