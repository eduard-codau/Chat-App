import { Component, OnInit } from '@angular/core';
import { Friend } from '../models/Friend/friend.model';
import { Jwt } from '../models/JWT/jwt.model';
import { ConversationsService } from '../services/Conversation/conversations.service';
import { UserService } from '../services/User/user.service';

@Component({
  selector: 'friends-for',
  template: `
    <div *ngFor="let person of friends">
      <div  (click)="onFriendClick(person.userId, person.chatId)">
        <div class="friend">
          <img src={{person.profileImage}}>
          <span>{{ person.firstName }} {{ person.lastName }}</span>
        </div>
        <hr class="line-light">
      </div>
    </div>
   `,
  styleUrls: ['./friends-for.component.css']
})
export class FriendsForComponent implements OnInit {

  image = "https://www.nicepng.com/png/detail/73-730154_open-default-profile-picture-png.png"
  friends: Friend[] = []
  chatsId: any[] = []

  constructor(private conversationsService: ConversationsService, private userService: UserService) { }


  ngOnInit(): void {
    //incerc sa citesc jwt-ul
    var jwt_string = localStorage.getItem('jwt')
    if (jwt_string) {
      var jwt = new Jwt(jwt_string)

      this.conversationsService.getConversations(jwt).subscribe({
        next: receivedData => {
          this.friends = receivedData;
          //console.log(receivedData);

          for (let i = 0; i < this.friends.length; i++) {
            this.userService.getUserProfileImage(this.friends[i].userId).subscribe(
              res => {
                this.friends[i].profileImage = 'data:image/jpeg;base64,' + res.picByte;
              }
            );
          }

          localStorage.setItem("chatLength", String(this.friends.length));

        },
        error: error => {
          console.error('There was an error!', error);
          console.error('A aparut o eroare! Nu se poate incarca lista de conversatii.');
        }
      })
    }
  }

  onFriendClick(userId: number, chatId: number) {
    //id-ul prietenului
    localStorage.setItem('currentChat', String(userId));

    //id-ul oconversatiei
    localStorage.setItem('currentChatId', String(chatId));
  }
}