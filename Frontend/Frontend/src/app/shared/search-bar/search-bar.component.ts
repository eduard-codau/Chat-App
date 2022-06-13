import { Component, OnInit } from '@angular/core';
import {FormControl} from '@angular/forms';
import { Router } from '@angular/router';
import {Observable} from 'rxjs';
import {map, startWith} from 'rxjs/operators';
import { Friend } from '../models/Friend/friend.model';
import { UserService } from '../services/User/user.service';

@Component({
  selector: 'app-search-bar',
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.css']
})
export class SearchBarComponent implements OnInit {
  myControl = new FormControl();
  options: Friend[] = [];
  filteredOptions: Observable<string[]> | undefined;

  constructor(private userService: UserService, private router: Router){

  }

  ngOnInit() {
  
  }

  public onTextChanged(event: any) {
    let searchedString:string = event.target.value;
    this.options = []

    if(searchedString.length != 0){
        this.userService.searchForUsers(searchedString).subscribe(
          res => {
            
            this.options = res;   
            for (let i = 0; i <  this.options.length; i++) { 
              this.userService.getUserProfileImage(this.options[i].userId).subscribe(
                response => {
                  this.options[i].profileImage =  'data:image/jpeg;base64,' + response.picByte;      
                },
                error => {
                  this.options[i].profileImage =  'https://www.nicepng.com/png/detail/73-730154_open-default-profile-picture-png.png';
                }
              );
            }  

          },
          err => console.log("Probabil nu s-au gasit potriviri, csf..")
        );
    }
  }


  onFriendProfileClick(userId: Number){
    var loggedUsedfId = Number(localStorage.getItem('userId'));
    console.log(loggedUsedfId + " " + userId);
    if(userId == loggedUsedfId){
      this.router.navigate(['my-profile']);
    }
    else{
      localStorage.setItem('currentViewedProfile', String(userId));
      this.router.navigate(['user-profile']);
    }
  }

}
