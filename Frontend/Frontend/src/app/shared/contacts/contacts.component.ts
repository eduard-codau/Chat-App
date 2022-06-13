import { Component, OnInit } from '@angular/core';
import { Friend } from '../models/Friend/friend.model';
import { UserService } from '../services/User/user.service';

@Component({
  selector: 'contacts',
  template: `
    <div class="fr" *ngFor="let person of friends">
      <div class="friend">
        <img src={{person.profileImage}}>
        <span>{{person.firstName}} {{person.lastName}}</span>
      </div>
      <div>
        <button class="send-req-button" (click)="onSendRequestClicked(person.userId)">Adaugă</button><br>
      </div>
    </div>
    <hr class="line-light">
   `,
  styleUrls: ['./contacts.component.css']
})

export class ContactsForComponent implements OnInit {

  constructor(private userService: UserService) { }

  friends: Friend[] = [];

  ngOnInit(): void {
    var userId = Number(localStorage.getItem('userId'));
    this.userService.getSuggestedFriends(userId).subscribe({
      next: receivedData => {
        //console.log(receivedData);
        this.friends = receivedData;

        for (let i = 0; i < this.friends.length; i++) {
          this.userService.getUserProfileImage(this.friends[i].userId).subscribe(
            res => {
              this.friends[i].profileImage = 'data:image/jpeg;base64,' + res.picByte;
            },
            err => { this.friends[i].profileImage = "https://www.nicepng.com/png/detail/73-730154_open-default-profile-picture-png.png" }
          );
        }

      },
      error: error => {
        console.error('There was an error!', error);
        console.error('A aparut o eroare! Nu se poate incarca lista de conversatii.');
      }

    })
  }

  onSendRequestClicked(receiverId: any) {
    var userId = localStorage.getItem('userId');
    if (userId != null) {
      this.userService.sendFriendRequest(Number(userId), receiverId).subscribe(
        res => {
          window.location.reload();
        },
        error => {
          console.log(error);
        }
      );
    }
  }

}