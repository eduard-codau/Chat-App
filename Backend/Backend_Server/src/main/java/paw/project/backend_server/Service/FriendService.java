package paw.project.backend_server.Service;

import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import paw.project.backend_server.DAO.*;
import paw.project.backend_server.Model.*;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static javax.swing.text.html.HTML.Attribute.N;

@Service
public class FriendService {
    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private BlockRepository blockedRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatService chatService;
    // Metode pentru:
    // - adaugare prieten(trimitere cerere prietenie)(done)
    // - stergere prieten
    // - acceptare cerere prietenie(done)
    // - refuzare cerere prietenie(done)
    // - obtinerea tuturor prieteniilor

    // Mai trebuie adaugate metode pentru:
    // - blocare prieten
    // - deblocare prieten

    public ResponseEntity<?> sendFriendRequest(FriendRequest friendRequest)
    {
        //trebuie verificat sa nu existe deja o prietenie intre cei doi utilizatori
        Optional<Friend> existingFriendship = friendRepository.findFriendship(friendRequest.getSenderUserId(), friendRequest.getReceiverUserId());

        if(existingFriendship.isPresent())
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // validam daca exista ambii utilizatori
        Optional<User> sender = userRepository.findById(friendRequest.getSenderUserId());

        if(sender.isEmpty())
        {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Optional<User> receiver = userRepository.findById(friendRequest.getReceiverUserId());

        if(receiver.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // verificam daca utilizatorii nu s-au blocat intre ei
        Optional<BlockUser> existingBlock = blockedRepository.findBlockship(friendRequest.getSenderUserId(), friendRequest.getReceiverUserId());
        if(existingBlock.isPresent())
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Optional<FriendRequest> existingFriendRequest = friendRequestRepository.findFriendRequest(friendRequest.getSenderUserId(), friendRequest.getReceiverUserId());
        if(existingFriendRequest.isPresent())
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        friendRequestRepository.save(friendRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public ResponseEntity<?> deleteFriendRequest(FriendRequest friendRequest)
    {
        Optional<User> sender = userRepository.findById(friendRequest.getSenderUserId());

        if(sender.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nu s-a gasit utilizator cu ID-ul " + friendRequest.getSenderUserId());
        }

        Optional<User> receiver = userRepository.findById(friendRequest.getReceiverUserId());

        if(receiver.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nu s-a gasit utilizator cu ID-ul " + friendRequest.getReceiverUserId());
        }

        Optional<FriendRequest> existingFriendRequest = friendRequestRepository.findRequest(friendRequest.getSenderUserId(),friendRequest.getReceiverUserId());
        if(existingFriendRequest.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nu exsita cererea cu datele respective");
        }

        friendRequestRepository.deleteFriendRequest(friendRequest.getSenderUserId(),friendRequest.getReceiverUserId());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Cere de prietenie stearsa cu succes");
    }

    public ResponseEntity<?> acceptFriendRequest(FriendRequest friendRequest)
    {
        Optional<User> sender = userRepository.findById(friendRequest.getSenderUserId());

        if(sender.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nu s-a gasit utilizator cu ID-ul " + friendRequest.getSenderUserId());
        }

        Optional<User> receiver = userRepository.findById(friendRequest.getReceiverUserId());

        if(receiver.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nu s-a gasit utilizator cu ID-ul " + friendRequest.getReceiverUserId());
        }

        Optional<FriendRequest> existingFriendRequest = friendRequestRepository.findRequest(friendRequest.getSenderUserId(),friendRequest.getReceiverUserId());

        if(existingFriendRequest.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Nu exista o cerere de prietenie cu aceste date");
        }

        friendRepository.save(Friend
                .builder()
                .user1Id(friendRequest.getSenderUserId())
                .user2Id(friendRequest.getReceiverUserId())
                .build()
        );

        friendRequestRepository.deleteFriendRequest(friendRequest.getSenderUserId(), friendRequest.getReceiverUserId());
        chatService.beginConversation(new PairOfUsers(friendRequest.getSenderUserId(),friendRequest.getReceiverUserId()));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Cerere de prietenie acceptata cu succes");
    }

    public ResponseEntity<?> deleteFriend(Friend friend)
    {
        Optional<User> user1 = userRepository.findById(friend.getUser1Id());

        if(user1.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nu s-a gasit utilizator cu ID-ul " + friend.getUser1Id());
        }

        Optional<User> user2 = userRepository.findById(friend.getUser2Id());

        if(user2.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nu s-a gasit utilizator cu ID-ul " + friend.getUser2Id());
        }

        Optional<Friend> existingFriendship = friendRepository.findFriendship(friend.getUser1Id(),friend.getUser2Id());
        if(existingFriendship.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nu exista prietenia");
        }

        friendRepository.deleteFriendship(friend.getUser1Id(), friend.getUser2Id());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Prietenie incheiata cu succes");
    }

    public ResponseEntity<?> getAllFriends() {
        return ResponseEntity.status(HttpStatus.OK).body(friendRepository.findAll());
    }

    public ResponseEntity<?> getFriendsForUser(Integer userId) {
        Optional<User> user = userRepository.findById(userId);

        if(user.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nu s-a gasit utilizator cu ID-ul " + userId);
        }

        List<Friend> friends = friendRepository.getFriendsByAnyIds(userId);
        System.out.println(friends);
        List<FriendDTO> friendDTOs = new ArrayList<>();
//        List<Chat> chatsForUser = chatRepository.getAllChatsForPairOfUsers()User(userId);
//        System.out.println(chatsForUser);

        for(Friend friend:friends)
        {
            List<Chat> chatsForUser;
            Integer friendId;
            if(friend.getUser1Id() == userId)
            {
                chatsForUser = chatRepository.getAllChatsForPairOfUsers(userId,friend.getUser2Id());
                friendId = friend.getUser2Id();
            }
            else
            {
                chatsForUser = chatRepository.getAllChatsForPairOfUsers(userId,friend.getUser1Id());
                friendId = friend.getUser1Id();
            }

            for(Chat chat:chatsForUser)
            {
                if(chat.getChatId().getUserId() != userId)
                {
                    friendDTOs.add(
                            FriendDTO.builder()
                                    .friendshipId(friend.getFriendshipId())
                                    .friendId(friendId)
                                    .chatId(chat.getChatId().getId())
                                    .build()
                    );
                }
            }



        }
        return ResponseEntity.status(HttpStatus.OK).body(friendDTOs);
    }

    public ResponseEntity<?> blockUser(BlockUser blockUser) {
        Optional<User> user1 = userRepository.findById(blockUser.getByUserId());

        if(user1.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nu s-a gasit utilizator cu ID-ul " + blockUser.getByUserId());
        }

        Optional<User> user2 = userRepository.findById(blockUser.getToUserId());

        if(user2.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nu s-a gasit utilizator cu ID-ul " + blockUser.getToUserId());
        }

        // daca erau prieteni, se sterge prietenia
        Optional<Friend> friendship = friendRepository.findFriendship(blockUser.getByUserId(), blockUser.getToUserId());
        if(friendship.isPresent())
        {
            friendRepository.deleteFriendship(friendship.get().getUser1Id(),friendship.get().getUser2Id());
        }

        // se adauga in lista de perechi cu utilizatori blocati
        blockedRepository.save(blockUser);
        return ResponseEntity.status(HttpStatus.OK).body("Utilizator blocat cu succes: " + blockUser.getToUserId());
    }

    public ResponseEntity<?> unblockUser(BlockUser blockUser) {
        Optional<User> user1 = userRepository.findById(blockUser.getByUserId());

        if(user1.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nu s-a gasit utilizator cu ID-ul " + blockUser.getByUserId());
        }

        Optional<User> user2 = userRepository.findById(blockUser.getToUserId());

        if(user2.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nu s-a gasit utilizator cu ID-ul " + blockUser.getToUserId());
        }

        blockedRepository.deleteBlockship(blockUser.getByUserId(), blockUser.getToUserId());

        return ResponseEntity.status(HttpStatus.OK).body("Utilizator deblocat cu succes: " + blockUser.getToUserId());
    }

    public ResponseEntity<?> getFriendsCountByEmail(Email email) throws JSONException {
        Optional<User> user = userRepository.findByEmail(email.getEmail());

        if(user.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nu exsita utilizatorul cu email-ul specificat");
        }

        JSONObject json = new JSONObject();
        json.put("count",friendRepository.getFriendsByAnyIds(user.get().getUserId()).size());
        return ResponseEntity.status(HttpStatus.OK).body(json.toString());
    }

    public ResponseEntity<?> getFriendRequestsForUser(Integer userId) {
        Optional<User> user = userRepository.findById(userId);

        if(user.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nu s-a gasit utilizator cu ID-ul " + userId);
        }

        List<FriendRequest> requests = friendRequestRepository.getReceivedRequests(userId);
        List<UserProfileDTO> friendDetailsList = new ArrayList<>();

        for(FriendRequest request:requests)
        {
            UserProfile senderUserProfile = userProfileRepository.findById(request.getSenderUserId()).get();
            User senderUser = userRepository.findById(request.getSenderUserId()).get();

            friendDetailsList.add(UserProfileDTO.builder()
                    .userId(request.getSenderUserId())
                    .username(senderUser.getUsername())
                    .faculty(senderUserProfile.getFaculty())
                    .phoneNumber(senderUserProfile.getPhoneNumber())
                    .gender(senderUserProfile.getGender())
                    .avatarPath(senderUserProfile.getAvatarPath())
                    .email(senderUser.getEmail())
                    .firstName(senderUserProfile.getFirstName())
                    .lastName(senderUserProfile.getLastName())
                    .birthday(senderUserProfile.getBirthday())
                    .build()
            );
        }

        return ResponseEntity.status(HttpStatus.OK).body(friendDetailsList);
    }
}
