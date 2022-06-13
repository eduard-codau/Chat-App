package paw.project.backend_server.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import paw.project.backend_server.Model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u from User u WHERE u.email = ?1")
    public Optional<User> findByEmail(String email);
}