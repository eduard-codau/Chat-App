import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Friend } from '../models/Friend/friend.model';
import { AuthService } from '../services/Auth/auth-service.service';
import { UserService } from '../services/User/user.service';



@Component({
  selector: 'nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit {

  friendReqs: Friend[]= [
   
  ];
  constructor(private router: Router, private authService: AuthService, private userService: UserService) { }

  ngOnInit(): void {
    var userId = localStorage.getItem('userId');
    if(userId != null){
      this.userService.getFriendRequests(Number(userId)).subscribe(
        res => {
          console.log(res);
          this.friendReqs = res;

          for (let i = 0; i <  this.friendReqs.length; i++) { 
            this.userService.getUserProfileImage(this.friendReqs[i].userId).subscribe(
              response => {
                this.friendReqs[i].profileImage =  'data:image/jpeg;base64,' + response.picByte;      
              },
              error => {
                this.friendReqs[i].profileImage =  'https://www.nicepng.com/png/detail/73-730154_open-default-profile-picture-png.png';
              }
            );
          }  
        },
        error => {
          console.log("Eroare la primirea cereriilor de prietenie.");
        }
      );
    }
  }

  checkIfUserIsLogged(){
    var sJwt = localStorage.getItem('jwt');
    if(sJwt == null) {
      this.router.navigate(['/login']);
    }
    else
    { 
      //daca sutn deja autentificat si ma duc pe pagina de login, atunci voi fi redirectat la pagian de chat
      this.authService.checkJwt(sJwt)
      .subscribe(response => {
        this.router.navigate(['/my-profile/chat']);

      }, 
      error => {
        this.router.navigate(['/login']);
      });
    }
  }

  logOut(){
    localStorage.clear();
    this.router.navigate(['/login']);
  }

  onAcceptFriendRequest(senderId: Number){
    var userId = localStorage.getItem('userId');
    if(userId != null){
      this.userService.acceptFriendRequest(senderId, Number(userId)).subscribe(
        res => {
          window.location.reload();
        },
        error => {
          console.log("Eroare la acceptarea cererii de prietenie.");
        }
      );
    }
  }

  onDeleteFriendRequest(senderId: Number){
    var userId = localStorage.getItem('userId');
    if(userId != null){
      this.userService.deleteFriendRequest(senderId, Number(userId)).subscribe(
        res => {
          window.location.reload();
        },
        error => {
          console.log("Eroare la stergerea cererii de prietenie.");
        }
      );
    }
  }

}
