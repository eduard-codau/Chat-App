package paw.project.backend_server.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import paw.project.backend_server.Model.BlockUser;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface BlockRepository extends JpaRepository<BlockUser,Integer> {
    @Query("SELECT b FROM BlockUser b WHERE b.byUserId=?1 AND b.toUserId=?2 OR b.toUserId=?2 AND b.byUserId=?1")
    Optional<BlockUser> findBlockship(Integer senderUserId, Integer receiverUserId);

    @Transactional
    @Modifying
    @Query("DELETE FROM BlockUser b WHERE b.byUserId=?1 AND b.toUserId=?2 OR b.byUserId=?2 AND b.toUserId=?1")
    public void deleteBlockship(Integer senderUserId, Integer receiverUserId);
}
