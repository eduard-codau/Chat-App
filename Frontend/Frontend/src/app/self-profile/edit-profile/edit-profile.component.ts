import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/shared/services/Auth/auth-service.service';
import { UserService } from 'src/app/shared/services/User/user.service';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})
export class EditProfileComponent {

  image = "https://www.nicepng.com/png/detail/73-730154_open-default-profile-picture-png.png" 
  name = "Ionel Ionescu"
  username = "ionelionescu"
  birthdate = new Date(1999, 0, 1)
  friends = 1
  faculty: string | undefined= "Facultatea de Automatică și Calculatoare"
  description = "Lorem ipsum dolor sit amet, consectetur, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Lorem ipsum dolor sit amet, consectetur, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
  phone = "0755-555-555"
  email = "ionelionescu@student.tuiasi.ro"
  errorMessage = ""
  gender = ""

  facultyAbv : Map<string, string> 

  constructor(private router: Router, private authService: AuthService, private userService: UserService) {
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

  editProfile() {
    var firstName =  (<HTMLInputElement>document.getElementById("name")).value.split(' ')[0];
    var lastName =  (<HTMLInputElement>document.getElementById("name")).value.split(' ')[1];
    var username =  (<HTMLInputElement>document.getElementById("username")).value;
    var date =  (<HTMLInputElement>document.getElementById("date")).value;
    var status =  (<HTMLInputElement>document.getElementById("status")).value;
    var faculty =  (<HTMLInputElement>document.getElementById("faculty")).value;
    var phoneNumber =  (<HTMLInputElement>document.getElementById("phone")).value;
    var email =  (<HTMLInputElement>document.getElementById("email")).value;

    if(faculty == ""){
      this.errorMessage = "Alege o facultate!"
      return;
    }
    //mai multe verificari?

    var userId = localStorage.getItem("userId");

    var obj = {
      "userId" : userId,
      "firstName" : firstName,
      "lastName" : lastName,
      "username" : username,
      "date" : date,
      "status" : status,
      "faculty" : faculty,
      "phoneNumber" : phoneNumber,
      "email" : email,
      "gender" : this.gender
    };

    this.userService.updateProfile(obj).subscribe(response => 
      this.onSuccessRegister(response), error => this.onFailedRegister(error));
   }
 
   onSuccessRegister(resp: any){
     //daca totul e ok, navigam catre pagina de profil
     this.router.navigate(['/my-profile']);
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

  cancelEdit(){
    this.router.navigate(['/my-profile']);
  }

}
