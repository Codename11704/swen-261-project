import { Injectable } from '@angular/core';
import { Observable, async, of } from 'rxjs';
import { User } from '../objects/user';

import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Need } from '../objects/need';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  isLoggedIn = false;

  private loginUrl = 'http://localhost:8080/login/';

  constructor(private http: HttpClient) {
  
  }

  login(username: string, password: string): Observable<boolean>{  
    return this.http.get<boolean>(this.loginUrl + username + ',' + password);
  }

  createAccount(user: User): Observable<boolean>{
    return this.http.post<boolean>(this.loginUrl, JSON.stringify(user));
  }

  getCart(user: string): Observable<Map<number, number>> {
    return this.http.get<Map<number, number>>(this.loginUrl + "basket/?user=" + user);
  }

  addtoCart(user: string, need: Need, amount: number): Observable<boolean> {
    return this.http.post<boolean>(this.loginUrl + 'basket/add/'+ user +'/' + need.id, amount);
  }

  removeFromCart(user: string, id: number) {
    return this.http.delete<boolean>(this.loginUrl + "basket/remove/" + user + "/" +id.toString());
  }

  updateUsername(oldUser: string, newUser: string): Observable<boolean>{
    return this.http.post<boolean>(this.loginUrl + "user/" + oldUser, newUser);
  }

  updatePassword(user: string, newPass: string): Observable<User> {
    return this.http.post<User>(this.loginUrl + "pass/" + user, newPass);
  }

  deleteAccount(user: string): Observable<User> {
    return this.http.delete<User>(this.loginUrl + user)
  }
}
