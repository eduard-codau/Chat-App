package paw.project.backend_server.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import paw.project.backend_server.Model.UserProfile;
import paw.project.backend_server.Model.UserProfileDTO;

import java.util.List;

public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {
    List<UserProfile> findByFaculty(String faculty);
}
