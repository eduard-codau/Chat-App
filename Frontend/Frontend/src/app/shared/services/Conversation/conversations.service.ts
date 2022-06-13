import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Friend } from '../../models/Friend/friend.model';
import { Jwt } from '../../models/JWT/jwt.model';

@Injectable({
  providedIn: 'root'
})
export class ConversationsService {

  getConversationsAPI = 'http://localhost:8080/conversations/';
  getChatIdAPI = 'http://localhost:8080/chat-informations/';

  constructor(private http: HttpClient) { }

  //asta returneaza o lista de date legate de userii cu care am o conversatie(nume, username, etc)
  getConversations(jwt: Jwt) {
    var headers = new HttpHeaders().set('content-type', 'application/json')
    .set('Access-Control-Expose-Headers', 'Authorization, X-Custom, Jwt-Token')
    .set('Jwt-Token', jwt.jwt);

    let options = { headers: headers };

    var userId = localStorage.getItem('userId');

    

    return this.http.get<Friend[]>(this.getConversationsAPI + userId, options);  
  }

  //asta returneaza o lista de date despre conversatiile curente(diverse id-uri)
  getChats(jwt: Jwt) {
    var headers = new HttpHeaders().set('content-type', 'application/json')
    .set('Access-Control-Expose-Headers', 'Authorization, X-Custom, Jwt-Token')
    .set('Jwt-Token', jwt.jwt);

    let options = { headers: headers };

    var userId = localStorage.getItem('userId');
    

    return this.http.get<any>(this.getChatIdAPI + userId, options);  
  }
}
