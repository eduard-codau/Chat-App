package paw.project.backend_server.Controller;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import paw.project.backend_server.Model.UserProfile;
import paw.project.backend_server.Service.UserProfileService;

import paw.project.backend_server.Model.UserProfileDTO;

import java.util.List;

@RestController
public class UserProfileController {
    @Autowired
    private UserProfileService userService;

    @GetMapping("/api/users/profiles")
    public ResponseEntity<List<UserProfile>> getAllUserProfiles()
    {
        return userService.getAllUserProfiles();
    }

    @GetMapping("/api/users/profiles/{ID}")
    public ResponseEntity<?> getUserProfileById(@PathVariable Integer ID) throws JSONException
    {
        UserProfileDTO userProfileDTO = userService.getUserProfileById(ID);
        if(userProfileDTO != null)
        {
            return ResponseEntity.status(HttpStatus.OK).body(userProfileDTO);
        }
        else
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/api/users/profiles")
    public ResponseEntity<?> addUserProfile(@RequestBody UserProfile userProfile)
    {
        if(userService.addUserProfile(userProfile))
        {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        else
        {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @PutMapping("/api/users/profiles")
    public ResponseEntity<?> updateUserProfile(@RequestBody UserProfile userProfile)
    {
        if(userService.updateUserProfile(userProfile))
        {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        else
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/api/users/profiles/{ID}")
    public ResponseEntity<?> deleteUserProfile(@PathVariable Integer ID)
    {
        if(userService.deleteUserProfile(ID))
        {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        else
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/api/users/search")
    public ResponseEntity<?> searchUsers(@RequestParam String query)
    {
        List<UserProfileDTO> result = userService.searchUsers(query);
        if(result.size() < 1)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nu s-au gasit potriviri.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
