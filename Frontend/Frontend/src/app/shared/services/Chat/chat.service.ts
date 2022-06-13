import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Jwt } from '../../models/JWT/jwt.model';

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  apiUrl = 'http://localhost:8080/login-user'

  constructor(private http: HttpClient) { }

  createConversation(jwt: Jwt) {
    var headers = new HttpHeaders().set('content-type', 'application/json')
    .set('Access-Control-Expose-Headers', 'Authorization, X-Custom, Jwt-Token')
    .set('Jwt-Token', jwt.jwt);

    let options = { headers: headers };

    var userId = localStorage.getItem('userId');


    return this.http.get<any>(this.apiUrl, options);  
  }
}
