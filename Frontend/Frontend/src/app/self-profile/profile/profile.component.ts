import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/shared/services/Auth/auth-service.service';
import { UserService } from 'src/app/shared/services/User/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent {

  //toate datele de aici vor trebui luate de la serveru-ul de API

  image = "https://www.nicepng.com/png/detail/73-730154_open-default-profile-picture-png.png" 

  name = "Name Name"
  username = "naemname"
  birthdate = "01/01/1910"
  friends = "0"

  faculty: string | undefined = "A faculty"
  description = " No description"
  phone = "0755-555-555"
  email = "namename@student.tuiasi.ro"
  gender= "NA"

  facultyAbv : Map<string, string> 

  constructor(private userService: UserService, private router: Router, private authService: AuthService) { 
    this.facultyAbv = new Map<string, string>();

    this.facultyAbv.set("arh", "Arhitectură „G.M. Cantacuzino”");
    this.facultyAbv.set("ac", "Automatică şi Calculatoare",);
    this.facultyAbv.set("icp", "Inginerie Chimică și Protecția Mediului „Cristofor Simionescu”");
    this.facultyAbv.set("ci", "Construcţii şi Instalaţii");
    this.facultyAbv.set("cmmi", "Construcţii de Maşini şi Management Industrial");
    this.facultyAbv.set("etti", "Electronică, Telecomunicaţii şi Tehnologia Informaţiei");
    this.facultyAbv.set("hgi", "Hidrotehnică, Geodezie şi Ingineria Mediului");
    this.facultyAbv.set("ieea", "Inginerie Electrică, Energetică şi Informatică Aplicată");
    this.facultyAbv.set("mec", "Mecanică");
    this.facultyAbv.set("sim", "Ştiinţa şi Ingineria Materialelor");
    this.facultyAbv.set("dima", "Design Industrial și Managementul Afacerilor");

  }

  //la initializare, se apeleaza API-ul pentru a completa datele din profil
  ngOnInit() {
    //aici veerificam daca utilizatorul poate intra pe pagina
    var sJwt = localStorage.getItem('jwt');
    if(sJwt == null) {
      this.router.navigate(['/login']);
    }
    else
    { 
      //ma intereseaza doar mesajele de eroare
      //daca primesc eroare, inseamna ca trebuie sa trimit utilizatorul la pagina de login
      this.authService.checkJwt(sJwt)
      .subscribe(response => {
        console.log("Autentificare validata.")
      }, 
      error => {
        this.router.navigate(['/login']);
      });
    }

    var userId = localStorage.getItem('userId');
    if(userId != null) {
      this.userService.getUserDetails(Number(userId))
      .subscribe(response => this.onSuccessGet(response), error => this.onFailedGet(error));
    }
  }


  onSuccessGet(resp: any){
    console.log(resp)

    this.name = resp.firstName + " " + resp.lastName;
    this.username = resp.username;
    this.phone = resp.phoneNumber;
    this.faculty = this.facultyAbv.get(resp.faculty);
    this.email = resp.email;
    this.friends = resp.friendsCount;
    this.description = resp.status;
    this.gender = resp.gender;
    this.birthdate = resp.birthday;

    var userId = localStorage.getItem('userId');

    this.userService.getUserProfileImage(Number(userId)).subscribe(
      res => {
        //iau poza in reprezentarea base64
        this.image = 'data:image/jpeg;base64,' + res.picByte;       
      }
    );

  }

  onFailedGet(error: any){
    console.log('Eroare: ', error.status, error.message, error.error)
  }

}
