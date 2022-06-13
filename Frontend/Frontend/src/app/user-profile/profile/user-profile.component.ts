import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/shared/services/Auth/auth-service.service';
import { UserService } from 'src/app/shared/services/User/user.service';



@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {

  image = "https://www.nicepng.com/png/detail/73-730154_open-default-profile-picture-png.png"
  name = "John Doe"
  username = "johndoe"
  birthdate = "01/01/1999"
  friends = 1
  faculty: string | undefined = "Facultatea de Automatică și Calculatoare"
  description = "Lorem ipsum dolor sit amet, consectetur, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Lorem ipsum dolor sit amet, consectetur, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
  phone = "0755-555-555"
  email = "johndoe@student.tuiasi.ro"
  gender = "NA"

  userId = -1;
  facultyAbv : Map<string, string>

  constructor(private router: Router, private userService: UserService, private authService: AuthService) {
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

  ngOnInit(): void {
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

    this.userId = Number(localStorage.getItem('currentViewedProfile'));
    this.userService.getUserDetails(Number(this.userId))
    .subscribe(response => this.onSuccessGet(response), error => this.onFailedGet(error));


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

    this.userService.getUserProfileImage(Number(this.userId)).subscribe(
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
