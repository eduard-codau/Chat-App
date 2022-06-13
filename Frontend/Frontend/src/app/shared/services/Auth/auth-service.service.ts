import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Jwt } from '../../models/JWT/jwt.model';
import { User } from '../../models/User/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  loginUserURL = 'http://localhost:8080/login-user'
  registerUserURL = 'http://localhost:8080/register-user'
  checkJwtURL = 'http://localhost:8080/validate-token'

  constructor(private http: HttpClient) { }

  //in raspuns se va gasi jwt-ul daca totul a de
  loginUser(email:string, password:string ) {
    //var headers = new HttpHeaders().set('content-type', 'application/json')

    return this.http.post<User>(this.loginUserURL, { "email": email, "password": password}, {observe: 'response'})

  }


  registerUser(data: any) {
    data.role = "USER";
    console.log(data);

    var obj = {
      "user" : {
        "password": data.password,
        "email": data.email,
        "username": data.username
      },
      "userProfile" :{
        "firstName": data.firstName,
        "lastName": data.lastName,
        "phoneNumber": data.phoneNumber,
        "faculty": data.faculty,
        "gender": data.gender,
        "birthday": data.birthday,
      }
    };

    var headers = new HttpHeaders().set('content-type', 'application/json');

    const body=JSON.stringify(obj);
    console.log(body+"ok!!");
    return this.http.post(this.registerUserURL, body, {'headers': headers});
  }

  checkJwt(jwt: string){
    var headers = new HttpHeaders().set('content-type', 'application/json')
    .set('Access-Control-Expose-Headers', 'Authorization, X-Custom, Jwt-Token')
    .set('Authorization','Bearer ' + jwt);

    return this.http.get(this.checkJwtURL, {'headers': headers})
  }

}
