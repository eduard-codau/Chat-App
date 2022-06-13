package paw.project.backend_server.Service;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import paw.project.backend_server.DAO.UserProfileRepository;
import paw.project.backend_server.DAO.UserRepository;
import paw.project.backend_server.Domain.UserPrincipal;
import paw.project.backend_server.Enumeration.Role;
import paw.project.backend_server.Exceptions.Domain.UserNotFoundException;
import paw.project.backend_server.Model.User;
import paw.project.backend_server.Model.UserProfile;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static paw.project.backend_server.Constant.UserImplConstants.NO_USER_FOUND_BY_USERNAME;
import static paw.project.backend_server.Constant.UserImplConstants.RETURNING_FOUND_USER_BY_USERNAME;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UserService implements UserDetailsService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    private Logger LOGGER = LoggerFactory.getLogger(getClass());//

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByEmail(email);
        if(user.isEmpty()){
            LOGGER.error(NO_USER_FOUND_BY_USERNAME+email);
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME+email);
        }else{
            User newUser = user.get();
            newUser.setLastLoginDateDisplay(newUser.getLastLoginDate());
            newUser.setLastLoginDate(new Date());
            userRepository.save(newUser);
            UserPrincipal userPrincipal=new UserPrincipal(newUser);
            LOGGER.info(RETURNING_FOUND_USER_BY_USERNAME +email);

            return userPrincipal;
        }
    }

    public User insertUser(User user){
        user.setAuthorities(Role.ROLE_USER.getAuthorities());
        user.setNotLocked(true);
        user.setActive(true);
        String encodedPassword=bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        user.setRole(Role.ROLE_USER.name());

        return userRepository.save(user);
    }

    public UserProfile insertUserProfile(UserProfile userProfile){
        return userProfileRepository.save(userProfile);
    }

    public Optional<User> getUserById(Integer userId) throws JSONException, UserNotFoundException {
        Optional<User> user = userRepository.findById(userId);

        if(user.isEmpty())
        {
            throw new UserNotFoundException("No user found by id:"+userId);
        }
        return user;
    }
    public ResponseEntity<List<User>> getAllUsers()
    {
        List<User> users = userRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    public ResponseEntity<?> updateUser(User user) {
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
