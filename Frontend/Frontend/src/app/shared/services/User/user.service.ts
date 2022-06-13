import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Friend } from '../../models/Friend/friend.model';


@Injectable({
  providedIn: 'root'
})
export class UserService {
  getFriendsNumberAPI = 'http://localhost:8080/friends-number'

  getUserDetailsAPI = 'http://localhost:8080/get-profile/'

  //grija aici, de schimbat
  getProfileImageAPI = 'http://localhost:8081/api/image/get-picture/'

  uploadProfileImageAPI = 'http://localhost:8080/profile-image/upload/' ;

  searchForFriendsAPI = 'http://localhost:8080/search-friends' ;

  suggestFriendsAPI = 'http://localhost:8080/suggest-friends/' ;

  updateProfileAPI = 'http://localhost:8080/update-profile' ;

  sendRequestAPI = 'http://localhost:8080/send-request' ;

  getFriendRequestsAPI = 'http://localhost:8080/get-requests/' ;

  acceptFriendRequestsAPI = 'http://localhost:8080/accept-request' ;

  deleteFriendRequestsAPI = 'http://localhost:8080/delete-request' ;

  deleteFriendAPI = 'http://localhost:8080/delete-friend' ;

  constructor(private http: HttpClient) { }


  getFriendsNumber(data: any){
    var headers = new HttpHeaders().set('content-type', 'application/json')

    return this.http.get(this.getFriendsNumberAPI, {'headers': headers});
  }


  getUserDetails(userId: number){
    var headers = new HttpHeaders().set('content-type', 'application/json')
    var url = this.getUserDetailsAPI + userId
    return this.http.get<Friend>(url, {'headers': headers});
  }


  getUserProfileImage(userId: number){
    return this.http.get<any>(this.getProfileImageAPI + userId)
  }

  uploadProfileImage(userId: number, uploadImageData: FormData){
    return this.http.post<any>(this.uploadProfileImageAPI + userId, uploadImageData, { observe: 'response' })
  }

  searchForUsers(user: string){
    let params = new HttpParams();
    params = params.append('query', user);

    return this.http.get<Friend[]>(this.searchForFriendsAPI, {params: params})
  }

  getSuggestedFriends(userId: number){
    return this.http.get<any>(this.suggestFriendsAPI + userId)
  }

  updateProfile(obj: any){
    var headers = new HttpHeaders().set('content-type', 'application/json');

    const body=JSON.stringify(obj);
    console.log(body);
    return this.http.post(this.updateProfileAPI, body, {'headers': headers});
  }

  sendFriendRequest(from: Number, to: Number){
    var headers = new HttpHeaders().set('content-type', 'application/json');
    
    var body = {
      "senderUserId" : from,
      "receiverUserId" : to
    }
    const body_json=JSON.stringify(body);
    console.log(body_json);
    return this.http.post(this.sendRequestAPI, body_json, {'headers': headers});
  }

  getFriendRequests(userdId: Number){
    var headers = new HttpHeaders().set('content-type', 'application/json')

    return this.http.get<Friend[]>(this.getFriendRequestsAPI + userdId, {'headers': headers});
  }

  acceptFriendRequest(from: Number, to: Number){
    var headers = new HttpHeaders().set('content-type', 'application/json');
    
    var body = {
      "senderUserId" : from,
      "receiverUserId" : to
    }
    const body_json=JSON.stringify(body);
    console.log(body_json);
    return this.http.post(this.acceptFriendRequestsAPI, body_json, {'headers': headers});
  }

  deleteFriend(from: Number, to: Number){
    var headers = new HttpHeaders().set('content-type', 'application/json');
    
    var body = {
      "senderUserId" : from,
      "receiverUserId" : to
    }
    
    const body_json=JSON.stringify(body);
    console.log(body_json);
    return this.http.post(this.deleteFriendAPI, body_json, {'headers': headers});
  }

  deleteFriendRequest(from: Number, to: Number){
    var headers = new HttpHeaders().set('content-type', 'application/json');
    
    var body = {
      "senderUserId" : from,
      "receiverUserId" : to
    }
    const body_json=JSON.stringify(body);
    console.log(body_json);
    return this.http.post(this.deleteFriendRequestsAPI, body_json, {'headers': headers});
  }
  
}
