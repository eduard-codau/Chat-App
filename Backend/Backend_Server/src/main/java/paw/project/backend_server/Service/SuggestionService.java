package paw.project.backend_server.Service;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import paw.project.backend_server.DAO.FriendRepository;
import paw.project.backend_server.DAO.FriendRequestRepository;
import paw.project.backend_server.DAO.UserProfileRepository;
import paw.project.backend_server.Model.*;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

@Service
public class SuggestionService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;


    public ResponseEntity<?> getSuggestionByFaculty(Integer id) throws JSONException {
        UserProfile user = userProfileRepository.getById(id);
        List<UserProfile> users = userProfileRepository.findByFaculty(user.getFaculty());
        List<UserProfile> otherUsers = userProfileRepository.findAll();
        users.remove(user);
        otherUsers.remove(user);

        List<UserProfile> usersArray = new ArrayList<>();

        if(!users.isEmpty()){
            for (UserProfile profile : users) {
                if(friendRepository.findFriendship(id, profile.getUserId()).isEmpty() && friendRequestRepository.findRequest(id, profile.getUserId()).isEmpty()){
                    usersArray.add(profile);
                }
            }
        }
        for(UserProfile profile:otherUsers){

            if(friendRepository.findFriendship(id, profile.getUserId()).isEmpty() && !users.contains(profile) && friendRequestRepository.findRequest(id, profile.getUserId()).isEmpty()) {
                usersArray.add(profile);
                System.out.print(profile);
            }
    }

        return ResponseEntity.status(HttpStatus.OK).body(usersArray);
    }
}
