import { Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from 'src/app/shared/services/User/user.service';
import * as io from 'socket.io-client';
import { Message } from 'src/app/shared/models/Message/message.model';
import { AuthService } from 'src/app/shared/services/Auth/auth-service.service';
import { MessageService } from 'src/app/shared/services/Message/message.service';
import { Jwt } from 'src/app/shared/models/JWT/jwt.model';

@Component({
  selector: 'app-chat-page',
  templateUrl: './chat-page.component.html',
  styleUrls: ['./chat-page.component.css']
})
export class ChatPageComponent implements OnInit {

  image = "https://www.nicepng.com/png/detail/73-730154_open-default-profile-picture-png.png"
  friendimage = "https://www.nicepng.com/png/detail/73-730154_open-default-profile-picture-png.png"

  friend!: string;
  friendId = -1;
  chatId = 1;

  page = 1;
  itemsPerPage = 10;

  friendUsername: string = "";

  messages: Message[];

  userId = -1;
  chatLength = -1;
  socket: io.Socket;

  @ViewChildren("messageContainer") messageContainers: QueryList<ElementRef>;
  @ViewChild('scrollMe') private myScrollContainer: ElementRef;

  constructor(private userService: UserService, private authService: AuthService, private router: Router, private messageService: MessageService) {
    this.messages = new Array<Message>();
    this.socket = io.connect("http://localhost:8000/");
    this.chatId = Number(localStorage.getItem("currentChatId"));
    this.userId = Number(localStorage.getItem('userId'));
    this.friendId = Number(localStorage.getItem('currentChat'));
    this.messageContainers = new QueryList<ElementRef>();
    this.myScrollContainer = new ElementRef(this.messageContainers);
  }

  scrollToBottom() {
    try {
      this.myScrollContainer.nativeElement.scrollTop = this.myScrollContainer.nativeElement.scrollHeight;
    } catch (err) { }
  }

  ngAfterViewInit() {
    this.messageContainers.changes.subscribe((list: QueryList<ElementRef>) => {
        this.scrollToBottom();
    });
  }

  ngOnInit() {
    //aici veerificam daca utilizatorul poate intra pe pagina
    var sJwt = localStorage.getItem('jwt');
    if (sJwt == null) {
      this.router.navigate(['/login']);
    }
    else {
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

    this.chatLength = Number(localStorage.getItem('chatLength'));
    //iau poza user-ului logat de profil
    this.userService.getUserProfileImage(Number(this.userId)).subscribe(
      res => {
        //iau poza in reprezentarea base64
        this.image = 'data:image/jpeg;base64,' + res.picByte;
      }
    );

    this.socket.on('receive-message', data => {
      var newMessage = new Message(data.text);
      console.log(newMessage)
      console.log(data)
      newMessage.chatId = data.chatId;
      newMessage.username = data.username;
      newMessage.sentAt = new Date(data.send_moment).toDateString();

      if (data.fromUserId == this.friendId) {
        this.messages.push(newMessage);
      }
    })

    this.page = 1;
    if (this.chatLength > 0) {
      this.changeChatView();
    }
    else if(this.chatLength == -1){
      location.reload();
    }
  }

  onFriendProfileClick() {
    this.friendId = Number(localStorage.getItem("currentChat"));
    localStorage.setItem('currentViewedProfile', String(this.friendId));
    this.router.navigate(['user-profile']);
  }

  async changeChatView() {
    this.chatLength = Number(localStorage.getItem('chatLength'));
    this.chatId = Number(localStorage.getItem("currentChatId"));
    this.userId = Number(localStorage.getItem('userId'));
    this.friendId = Number(localStorage.getItem('currentChat'));

    //iau poza prietenului
    this.userService.getUserProfileImage(Number(this.friendId)).subscribe(
      res => {
        //iau poza in reprezentarea base64
        this.friendimage = 'data:image/jpeg;base64,' + res.picByte;
      }
    );
    this.userService.getUserDetails(Number(this.friendId)).subscribe(
      res => {
        this.friend = res.lastName + ' ' + res.firstName;
        this.friendUsername = res.username;

        this.socket.emit('init', {
          userId: this.userId,
          username: localStorage.getItem('username'),
          friendId: this.friendId,
          friendName: this.friendUsername
        })

      }
    );

    this.messages = [];
    this.page = 1;
    var sJwt = localStorage.getItem('jwt');
    if (sJwt == null) {
      this.router.navigate(['/login']);
    }
    else {
      
      this.messageService.getMessages(this.chatId, this.page, this.itemsPerPage, new Jwt(sJwt)).subscribe(
        res => {
          //console.log(res);
          this.messages = res;

          this.messages.reverse();

          for (let i = 0; i < this.messages.length; i++) {
            if (this.messages[i].fromUser == this.userId) {
              this.messages[i].username = localStorage.getItem('username');
            }
            else {
              this.messages[i].username = this.friendUsername;
            }
          }

          (<HTMLInputElement>document.getElementById("test")).scrollTop  = 200;
          console.log( (<HTMLInputElement>document.getElementById("test")));
        },
        error => {
          console.log("Nu sunt mesaje pentru acest prieten.");
        }
      );
    }
  }

  onSelfProfileClick() {
    this.router.navigate(['my-profile']);

  }

  sendMessage(message: string) {
    if (message.length > 0) {
      var username = localStorage.getItem('username');
      var chatId = localStorage.getItem('currentChatId');

      if (username != null && chatId != null) {
        var newMessage = new Message(message);
        newMessage.username = username;
        newMessage.fromUser = this.userId;

        newMessage.sentAt = new Date().toDateString();

        this.messages.push(newMessage);
        console.log("mesaj trimis");

        this.socket.emit('send-message', {
          chatId: chatId,
          fromUserId: this.userId,
          text: message,
          username: username
        });
        (<HTMLInputElement>document.getElementById("chat_text")).value = "";
      }
    }
  }

  onScroll(scroll: any){
    var sJwt = localStorage.getItem('jwt');
    if(sJwt != null){
      if( this.myScrollContainer.nativeElement.scrollTop == 0)
      {
        console.log("se apel");

        this.page += 1;
        this.messageService.getMessages(this.chatId, this.page, this.itemsPerPage, new Jwt(sJwt)).subscribe(
          res => {
            //console.log(res);
            var newMessages: Message[] = res;

            //this.messages.reverse();

            for (let i = 0; i < newMessages.length; i++) {
              if (this.messages[i].fromUser == this.userId) {
                newMessages[i].username = localStorage.getItem('username');
              }
              else {
                newMessages[i].username = this.friendUsername;
              }
              this.messages.unshift(newMessages[i]);
            }
          },
          
          error => {
            console.log("Nu sunt mesaje pentru acest prieten.");
          }
        );
      }
    }
  }
}