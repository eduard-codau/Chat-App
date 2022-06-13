package paw.project.backend_server.Service;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import paw.project.backend_server.DAO.UserProfileRepository;
import paw.project.backend_server.DAO.UserRepository;
import paw.project.backend_server.Model.Email;
import paw.project.backend_server.Model.User;
import paw.project.backend_server.Model.UserProfile;
import paw.project.backend_server.Model.UserProfileDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendService friendService;

    public ResponseEntity<List<UserProfile>> getAllUserProfiles()
    {
        List<UserProfile> users = userProfileRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    public boolean addUserProfile(UserProfile userProfile)
    {
        try
        {
            userProfileRepository.save(userProfile);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public UserProfileDTO getUserProfileById(Integer userId) throws JSONException
    {
        Optional<UserProfile> userProfile = userProfileRepository.findById(userId);

        if(userProfile.isEmpty())
        {
            return null;
        }

        UserProfile existingUserProfile = userProfile.get();
        User user = userRepository.getById(existingUserProfile.getUserId());
        ResponseEntity<?> friendsCount = friendService.getFriendsCountByEmail(new Email(user.getEmail()));
        JSONObject object = new JSONObject(friendsCount.getBody().toString());
        int count = object.getInt("count");
        UserProfileDTO userProfileDTO = UserProfileDTO.builder()
                .username(user.getUsername())
                .userId(userId)
                .avatarPath(existingUserProfile.getAvatarPath())
                .birthday(existingUserProfile.getBirthday())
                .faculty(existingUserProfile.getFaculty())
                .email(user.getEmail())
                .firstName(existingUserProfile.getFirstName())
                .lastName(existingUserProfile.getLastName())
                .gender(existingUserProfile.getGender())
                .phoneNumber(existingUserProfile.getPhoneNumber())
                .status(existingUserProfile.getStatus())
                .friendsCount(count)
                .build();

        return userProfileDTO;
    }

    public boolean updateUserProfile(UserProfile userProfile)
    {
        try{
            Optional<UserProfile> user = userProfileRepository.findById(userProfile.getUserId());
            if(user.isPresent())
            {
                UserProfile existingUserProfile = user.get();
                existingUserProfile.setFirstName(userProfile.getFirstName());
                existingUserProfile.setLastName(userProfile.getLastName());
                existingUserProfile.setStatus(userProfile.getStatus());
                existingUserProfile.setFaculty(userProfile.getFaculty());
                existingUserProfile.setBirthday(userProfile.getBirthday());
                existingUserProfile.setPhoneNumber(userProfile.getPhoneNumber());
                existingUserProfile.setGender(userProfile.getGender());
                existingUserProfile.setAvatarPath(userProfile.getAvatarPath());
                userProfileRepository.save(existingUserProfile);
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUserProfile(Integer userId)
    {
        Optional<UserProfile> user = userProfileRepository.findById(userId);
        if(user.isPresent())
        {
            userProfileRepository.deleteById(userId);
            return true;
        }
        else
        {
            return false;
        }
    }

    public List<UserProfileDTO> searchUsers(String queryString)
    {
        List<UserProfileDTO> result = new ArrayList<>();
        List<UserProfile> profiles = userProfileRepository.findAll();
        List<User> users = userRepository.findAll();

        for(int i=0;i<users.size();i++)
        {
            if(profiles.get(i).getFirstName().toLowerCase().contains(queryString.toLowerCase()) ||
                    profiles.get(i).getLastName().toLowerCase().contains(queryString.toLowerCase()) ||
                    users.get(i).getUsername().toLowerCase().contains(queryString.toLowerCase()))
            {
                result.add(
                        UserProfileDTO.builder()
                                .userId(profiles.get(i).getUserId())
                                .username(users.get(i).getUsername())
                                .email(users.get(i).getEmail())
                                .firstName(profiles.get(i).getFirstName())
                                .lastName(profiles.get(i).getLastName())
                                .status(profiles.get(i).getStatus())
                                .faculty(profiles.get(i).getFaculty())
                                .gender(profiles.get(i).getGender())
                                .birthday(profiles.get(i).getBirthday())
                                .avatarPath(profiles.get(i).getAvatarPath())
                                .phoneNumber(profiles.get(i).getPhoneNumber())
                                .build()
                );
            }
        }

        return result;
    }
}
