import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/shared/services/Auth/auth-service.service';
import { Router } from '@angular/router';
import { DatePipe } from '@angular/common';
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  errorMessage = ""

  constructor( private authService: AuthService, private router: Router) { }

  ngOnInit(): void {

  }



  registerUser(data: any){
    const dateSendingToServer = new DatePipe('en-US').transform(data.birthday, 'yyyy-MM-dd')
    console.log(dateSendingToServer);
    //de adaugat toate tipurile de erori
    if(data.firstName == "" || data.firstName.length < 2){
      this.errorMessage = "Va rugam introduceti un nume valid!";
      window.scroll(0,0);
      return
    }
    if(data.lastName == ""  || data.lastName.length < 2){
      this.errorMessage = "Va rugam introduceti un prenume!";
      window.scroll(0,0);
      return
    }
    if(data.email == ""){
      this.errorMessage = "Va rugam introduceti un email valid!";
      window.scroll(0,0);
      return
    }

    this.authService.registerUser(data).subscribe(response =>
      this.onSuccessRegister(response), error => this.onFailedRegister(error));
   }

   onSuccessRegister(resp: any){
     //daca totul e ok, navigam catre pagina de chat
     this.router.navigate(['/login']);
   }

   onFailedRegister(error: any){
     console.log('Eroare: ', error.status, error.message, error.error)

     //de adaugat alte erori(daca e cazul)
     if(error.status == 400){
       this.errorMessage = error.error.message
     }
     if(error.status == 0){
       this.errorMessage = "Serverul nu raspunde! Contacteaza administratorul."
     }
   }

}
