import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  public registerForm!: FormGroup;
  response: String = "";
  
  constructor(private authService: AuthService, private router: Router) {

  }

  ngOnInit() {
    this.registerForm = new FormGroup({
      username: new FormControl(''),
      password: new FormControl(''),
    });
  }

  public onSubmit() {
    this.response = ""
    if(this.registerForm.get('username')!.value.trim() === "") {
      this.response = "Please Enter a Username"
    } else if (this.registerForm.get('password')!.value.trim() === "") {
      this.response = "Please Enter a Password"
    } else if (!this.authService.createAccount(this.registerForm.get('username')!.value.trim(), this.registerForm.get('password')!.value.trim())) {
      this.response = "User is taken"
    }
  }

  public back() {
    this.router.navigate(['./login']);
  }

}
