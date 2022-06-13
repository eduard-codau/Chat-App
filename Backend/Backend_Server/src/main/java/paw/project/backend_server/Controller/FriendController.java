package paw.project.backend_server.Controller;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import paw.project.backend_server.Model.BlockUser;
import paw.project.backend_server.Model.Email;
import paw.project.backend_server.Model.Friend;
import paw.project.backend_server.Model.FriendRequest;
import paw.project.backend_server.Service.FriendService;

@RestController
public class FriendController {
    @Autowired
    private FriendService friendService;

    // de adaugat srviciu de returnare a numarului de prieteni al unui utilizator pe baza adresei de email
    @PostMapping("/api/friends/friendscount")
    public ResponseEntity<?> getFriendsCountByEmail(@RequestBody Email email) throws JSONException
    {
        return friendService.getFriendsCountByEmail(email);
    }

    @GetMapping("/api/friends/friendrequests/{userId}")
    public ResponseEntity<?> getFriendRequestsForUser(@PathVariable Integer userId)
    {
        return friendService.getFriendRequestsForUser(userId);
    }

    @GetMapping("/api/friends")
    public ResponseEntity<?> getAllFriends()
    {
        return friendService.getAllFriends();
    }

    @GetMapping("/api/friends/{UserId}")
    public ResponseEntity<?> getFriendsForUser(@PathVariable Integer UserId)
    {
        return friendService.getFriendsForUser(UserId);
    }

    @PostMapping("/api/friends/addfriend")
    public ResponseEntity<?> sendFriendRequest(@RequestBody FriendRequest friendRequest)
    {
        return friendService.sendFriendRequest(friendRequest);
    }

    @DeleteMapping("/api/friends/deletefriend")
    public ResponseEntity<?> deleteFriend(@RequestBody Friend friend)
    {
        return friendService.deleteFriend(friend);
    }

    @PostMapping("/api/friends/acceptrequest")
    public ResponseEntity<?> acceptFriendRequest(@RequestBody FriendRequest friendRequest)
    {
        return friendService.acceptFriendRequest(friendRequest);
    }

    @DeleteMapping("/api/friends/deleterequest")
    public ResponseEntity<?> deleteFriendRequest(@RequestBody FriendRequest friendRequest)
    {
        return friendService.deleteFriendRequest(friendRequest);
    }

    @PostMapping("/api/friends/blockuser")
    public ResponseEntity<?> blockUser(@RequestBody BlockUser blockUser)
    {
        return friendService.blockUser(blockUser);
    }

    @PostMapping("/api/friends/unblockuser")
    public ResponseEntity<?> unblockUser(@RequestBody BlockUser blockUser)
    {
        return friendService.unblockUser(blockUser);
    }
}
