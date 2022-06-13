package paw.project.backend_server.Controller;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import paw.project.backend_server.DAO.UserRepository;
import paw.project.backend_server.Domain.UserPrincipal;
import paw.project.backend_server.Exceptions.Domain.EmailExistException;
import paw.project.backend_server.Exceptions.Domain.ExceptionHandling;
import paw.project.backend_server.Exceptions.Domain.UserNotFoundException;
import paw.project.backend_server.Exceptions.Domain.UsernameExistException;
import paw.project.backend_server.Model.User;
import paw.project.backend_server.Model.UserDTO;
import paw.project.backend_server.Model.UserProfile;
import paw.project.backend_server.Service.UserService;
import paw.project.backend_server.Utility.JWTTokenProvider;

import java.util.List;
import java.util.Optional;

import static paw.project.backend_server.Constant.SecurityConstant.JWT_TOKEN_HEADER;
import static paw.project.backend_server.Constant.UserImplConstants.NO_USER_FOUND_BY_USERNAME;

@RestController
@RequestMapping(path="/api/user")
public class UserAuthController extends ExceptionHandling {

    private UserService userService;
    private AuthenticationManager authenticationManager;
    private JWTTokenProvider jwtTokenProvider;
    private UserRepository userRepository;

    @Autowired
    public UserAuthController(UserService userService, AuthenticationManager authenticationManager, JWTTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }
    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers=new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER,jwtTokenProvider.generateJwtToken(user));
        return headers;

    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));

    }



    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user)  {

        authenticate(user.getEmail(),user.getPassword());
        Optional<User> loginUser=userRepository.findUserByEmail(user.getEmail());

        if(loginUser.isEmpty())
        {
            //ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            throw new BadCredentialsException(NO_USER_FOUND_BY_USERNAME+user.getEmail());
        }
        User newUser = loginUser.get();
        UserPrincipal userPrincipal=new UserPrincipal(newUser);

        //generate headers for JWT
        HttpHeaders jwtHeader=getJwtHeader(userPrincipal);
        jwtHeader.add("Access-Control-Expose-Headers", "Authorization, X-Custom, Jwt-Token");

        return new ResponseEntity<>(newUser, jwtHeader, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) throws UsernameExistException, EmailExistException {
        System.out.println(userDTO);
        //User newUser=userService.insertUser(userProfile.getUsername(),userProfile.getEmail() ,userProfile.getPassword());
        if(findUserByUsername(userDTO.getUser().getUsername()).isPresent()){
            System.out.println(userDTO.getUser().getUsername());
            throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
        }
        else if(findUserByEmail(userDTO.getUser().getEmail()).isPresent()){
            throw new EmailExistException(EMAIL_ALREADY_EXISTS);
        }else {
            User newUser = userService.insertUser(userDTO.getUser());
            UserProfile newUserProfile = userDTO.getUserProfile();
            newUserProfile.setUserId(newUser.getUserId());

            UserProfile newUser1 = userService.insertUserProfile(newUserProfile);

            return new ResponseEntity<>(newUser1, HttpStatus.OK);
        }

    }
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestHeader(value="Authorization") String tokenString)
    {
        String[] token = tokenString.split(" ");

        if(!token[0].equals("Bearer"))
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Header invalid");
        }
        System.out.println(token);
        List<GrantedAuthority> authorities = jwtTokenProvider.getAuthorities(token[1]);
        String username = jwtTokenProvider.getSubject(token[1]);
        if(jwtTokenProvider.isTokenValid(username,token[1]))
        {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        else
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalid");
        }
    }

    @GetMapping("/user/{ID}")
    public ResponseEntity<?> getUserById(@PathVariable Integer ID) throws JSONException, UserNotFoundException {
        Optional<User> userDTO = userService.getUserById(ID);
        if(userDTO != null)
        {
            return ResponseEntity.status(HttpStatus.OK).body(userDTO);
        }
        else
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers()
    {
        return userService.getAllUsers();
    }

    @PostMapping("/updateuser")
    public ResponseEntity<?> updateUser(@RequestBody User user) { return userService.updateUser(user);}
}
