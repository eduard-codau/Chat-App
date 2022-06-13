package paw.project.backend_server.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import paw.project.backend_server.Model.Friend;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend,Integer> {
    @Query("SELECT f FROM Friend f WHERE f.user1Id=?1 or f.user2Id=?1")
    public List<Friend> getFriendsByAnyIds(Integer userId);

    @Query("SELECT f FROM Friend f WHERE f.user1Id=?1 AND f.user2Id=?2 OR f.user1Id=?2 AND f.user2Id=?1")
    public Optional<Friend> findFriendship(Integer user1Id, Integer user2Id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Friend f WHERE f.user1Id=?1 AND f.user2Id=?2 OR f.user1Id=?2 AND f.user2Id=?1")
    void deleteFriendship(Integer user1Id, Integer user2Id);

}
