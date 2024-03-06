import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/auth/auth.service';

@Component({
  selector: 'app-user-settings',
  templateUrl: './user-settings.component.html',
  styleUrls: ['./user-settings.component.css']
})
export class UserSettingsComponent {

  userForm!: FormGroup;
  userResponse: String = ""
  passResponse: String = ""
  

  constructor(private authService: AuthService, private router: Router) {
    this.userForm = new FormGroup({
      newUser: new FormControl(''),
      newPass: new FormControl('')
    });
  }

  getUser(): string | undefined {
    return this.authService.getCurrentUser()
  }

  onChangeUser() {
    this.userResponse = ""

    if(this.userForm?.get('newUser')?.value.trim() === "") {
      this.userResponse = "Please Enter A Username"
    } else if(!this.authService.updateUser(this.userForm?.get('newUser')?.value.trim())) {
      this.userResponse = "Username Already Taken"
    }
    
  }

  onChangePass() {
    this.passResponse = ""

    if(this.userForm?.get('newPass')?.value.trim() === "") {
      this.passResponse = "Please Enter A Password"
    } else {
      this.authService.updatePass(this.userForm?.get('newPass')?.value.trim())
      this.passResponse = "Password Successfully Changed"
      this.userForm?.get('newPass')?.setValue("")
    }
    
  }

  onDelete() {
    this.authService.deleteAccount();
    this.router.navigate(['login'])
  }

  back() {
    this.router.navigate(['cupboard']);
  }

  logout() {
    this.router.navigate(['login'])
  }
}

