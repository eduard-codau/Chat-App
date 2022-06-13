package paw.project.backend_server.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import paw.project.backend_server.Model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    //@Query("select u from User u where u.email = ?1")
    Optional<User> findUserByEmail(String email);
    //@Query("select u from User u where u.username = ?1")
    Optional<User> findUserByUsername(String username);
}
