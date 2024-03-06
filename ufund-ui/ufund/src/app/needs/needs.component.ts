import { Component } from '@angular/core';
import { Need } from '../objects/need';
import { NeedService } from '../needService/need.service';
import { User } from '../objects/user';
import { AuthService } from '../auth/auth.service';
import { CheckoutService } from './checkout.service';
import { Router } from '@angular/router';
import { Form, FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { HttpClient, HttpHandler } from '@angular/common/http';

@Component({
  selector: 'app-needs',
  templateUrl: './needs.component.html',
  styleUrls: ['./needs.component.css']
})
export class NeedsComponent {
  needs: Need[] = [];
  form!: FormGroup;
  search!: FormGroup;
  noNeeds: String = ""

  constructor(private needService: NeedService, private authService: AuthService, private checkoutService: CheckoutService, private router: Router, private fb: FormBuilder) {
    
  }

  ngOnInit(): void {
    this.form = this.fb.group({
      'needForms': new FormArray([])
    }) 
    this.search = new FormGroup({
      searchValue: new FormControl('')
    })
    this.getNeeds();
  }

  get needsFormArray(): FormArray {
    return this.form.get('needForms') as FormArray;

  }

  getResponse(i: number): String{
    return this.needsFormArray.get(i.toString())?.get('response')?.value
  }

  getNeeds(): void {
    this.needService.getNeeds().subscribe((needs) => {
      this.needs = needs;
      for(let i = 0; i < needs.length; i++) {
        this.needsFormArray.push(this.fb.group({
          'amount': '',
          'response': ''
        }));
      }
      if(this.needs.length == 0) {
        this.noNeeds = "There are currently no Needs"
      }
    });
  }

  searchNeeds(search: string): void {
    this.needService.searchNeeds(search.trim()).subscribe((needs) => {
      this.needs = needs;
      for(let i = 0; i < needs.length; i++) {
        this.needsFormArray.push(this.fb.group({
          'amount': '',
          'response': ''
        }));
      }
      if(this.needs.length == 0) {
        this.noNeeds = "There are currently no needs with " + search.trim()
      } else {
        this.noNeeds = ""
      }
    });
  }
  
  onSubmit(need: Need, i: number): void {
    this.getResponse(i)
    this.needsFormArray.get(i.toString())?.get('response')?.setValue("")
  
    let amount
    if(!isNaN(this.needsFormArray.get(i.toString())?.get('amount')?.value)) {
      amount = parseFloat(this.needsFormArray.get(i.toString())?.get('amount')?.value);
      if(amount > 0) {
        console.log(amount);
        this.authService.addToCart(need, amount);
        this.needsFormArray.get(i.toString())?.get('response')?.setValue("Successfully added to cart")
        this.needsFormArray.get(i.toString())?.get('amount')?.setValue("")
      } else {
        this.needsFormArray.get(i.toString())?.get('response')?.setValue("Please Enter A Valid Ammount")
      }
    } else {
      this.needsFormArray.get(i.toString())?.get('response')?.setValue("Please Enter A Valid Ammount")
    }
  }

  toUserSettings() {
    this.router.navigate(['user'])
  }

  proceedToCheckout() {
    this.router.navigate(['checkout']);
  }

  searchNeed() {
    this.searchNeeds(this.search.get('searchValue')?.value);
  }

  logout() {
    this.authService.logout();
  }
}