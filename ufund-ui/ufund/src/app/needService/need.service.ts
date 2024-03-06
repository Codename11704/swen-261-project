import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Need } from '../objects/need';

import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../auth/auth.service';


@Injectable({
  providedIn: 'root'
})
export class NeedService {

  private needsUrl = 'http://localhost:8080/cupboard';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient, private authService: AuthService) {
  
   }

  getNeeds(): Observable<Need[]> {
    return this.http.get<Need[]>(this.needsUrl);
  }

  searchNeeds(search: string): Observable<Need[]> {
    return this.http.get<Need[]>(this.needsUrl + "/?name=" + search);
  }

  updateNeed(need: Need): Observable<Need> {
    return this.http.put<Need>(this.needsUrl, need, this.httpOptions);
  }

  createNeed(need: Need): Observable<Need> {
    return this.http.post<Need>(this.needsUrl, need);
  }

  deleteNeed(id: string): Observable<Need> {
    return this.http.delete<Need>(this.needsUrl + "/" + id); 
  }

  checkout(cart: Map<string, number>) {
    console.log(JSON.stringify(Object.fromEntries(cart)));
    return this.http.post<Need[]>(this.needsUrl + "/checkout/" + this.authService.getCurrentUser, JSON.stringify(Object.fromEntries(cart)), this.httpOptions);
  }
}
