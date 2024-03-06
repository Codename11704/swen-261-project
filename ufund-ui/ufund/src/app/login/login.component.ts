import { Component } from '@angular/core';
import { User } from '../objects/user';
import { Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AppComponent } from '../app.component';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  public loginForm!: FormGroup;

  response: String = "";
  
  constructor(private authService: AuthService, private router: Router) {

  }

  ngOnInit() {
    this.loginForm = new FormGroup({
      username: new FormControl(''),
      password: new FormControl(''),
    });
  }

  public onSubmit() {
    this.response = ""

    if(this.loginForm.get('username')!.value.trim() === "") {
      this.response = "Please enter a username";
    } else if(this.loginForm.get('password')!.value.trim() === "") {
      this.response = "Please enter a password";
    } else 
      if(!this.authService.login(this.loginForm.get('username')!.value.trim(), this.loginForm.get('password')!.value.trim())) {
        this.response = "Username or password incorrect";
      }
  }

  public register() {
    this.router.navigate(['./register']);
  }


}
