import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from 'src/app/shared/models/User/user.model';
import { AuthService } from 'src/app/shared/services/Auth/auth-service.service';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private authService: AuthService, private router: Router) { }

  errorMessage: any | undefined = ""


  ngOnInit(): void {
    var sJwt = localStorage.getItem('jwt');
    if(sJwt == null) {
      this.router.navigate(['/login']);
    }
    else
    { 
      //daca sunt deja autentificat si ma duc pe pagina de login, atunci voi fi redirectat la pagian de chat
      this.authService.checkJwt(sJwt)
      .subscribe(response => {
        this.router.navigate(['/my-profile/chat']);

      }, 
      error => {
        this.router.navigate(['/login']);
      });
    }

  }

  login(data: any){
    if(data.username == ""){
      this.errorMessage = "Introduceti un email valid!";
      return;
    }
    if(data.password == ""){
      this.errorMessage = "Va rugam introduceti o parola!";
      return;
    }

    //daca totul e ok, salvez jwt-ul in local storage, daca nu, afisez eroarea respectiva
    this.authService.loginUser(data.email, data.password)
    .subscribe(response => this.onSuccessLogin(response), error => this.onFailedLogin(error));
  }

  onSuccessLogin(resp: any){
    var jwt = resp.headers.get('Jwt-Token')
    localStorage.setItem('jwt', jwt)
    console.log("S-a salvat cu succes token-ul: " + localStorage.getItem('jwt'));

    //salvez userId-ul in storage
    localStorage.setItem('userId', resp.body.userId);

    localStorage.setItem('username', resp.body.username);
    //daca totul e ok, navigam catre pagina de chat
    this.router.navigate(['/my-profile/chat']);
  }

  onFailedLogin(error: any){
    console.log(error);

    //de adaugat alte erori(daca e cazul)
    this.errorMessage = error.error.message
   
  }
}
