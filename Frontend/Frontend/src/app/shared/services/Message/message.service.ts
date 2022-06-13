import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Jwt } from '../../models/JWT/jwt.model';
import { Message } from '../../models/Message/message.model';

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  apiUrl = 'http://localhost:8080/get-messages/'

  constructor(private http: HttpClient) { }

  getMessages(chatId: Number, page:Number, itemsPerPage:number, jwt: Jwt) {
    var headers = new HttpHeaders().set('content-type', 'application/json')
    .set('Access-Control-Expose-Headers', 'Authorization, X-Custom, Jwt-Token')
    .set('Jwt-Token', jwt.jwt);

    let options = { headers: headers };

    return this.http.get<Message[]>(this.apiUrl + chatId  + "?page=" +page + "&itemsPerPage=" + itemsPerPage, options);  
  }
}
