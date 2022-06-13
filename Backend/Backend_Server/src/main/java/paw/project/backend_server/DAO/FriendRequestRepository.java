package paw.project.backend_server.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import paw.project.backend_server.Model.FriendRequest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest,Integer> {
    @Query("SELECT fr FROM FriendRequest fr WHERE fr.senderUserId=?1 AND fr.receiverUserId=?2")
    Optional<FriendRequest> findRequest(Integer senderUserId, Integer receiverUserId);

    @Query("SELECT fr FROM FriendRequest fr WHERE fr.receiverUserId=?1")
    List<FriendRequest> getReceivedRequests(Integer receiverUserId);

    @Query("SELECT fr FROM FriendRequest fr WHERE fr.senderUserId = ?1 AND fr.receiverUserId = ?2")
    Optional<FriendRequest> findFriendRequest(Integer senderId, Integer receiverId);

    @Modifying
    @Transactional
    @Query("DELETE FROM FriendRequest fr WHERE fr.senderUserId=?1 AND fr.receiverUserId=?2")
    public void deleteFriendRequest(Integer senderUserId, Integer receiverUserId);
}
