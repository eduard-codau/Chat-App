package paw.project.backend_server.DAO;


import org.springframework.data.jpa.repository.JpaRepository;
import paw.project.backend_server.Model.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {
}
