package paw.project.backend_server.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import paw.project.backend_server.Model.Chat;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat,Integer> {
    @Query("SELECT c FROM Chat c WHERE c.chatId.id = ?1")
    public List<Chat> getAllByChatId(Integer chatId);

    @Query("SELECT c FROM Chat c WHERE c.chatId.userId = ?1")
    public List<Chat> getAllChatsForUser(Integer userId);

    @Query("SELECT c from Chat c where c.chatId.id = (select max(c.chatId.id) from Chat c)")
    public Chat getChatByMaxId();

    @Query("SELECT c from Chat c WHERE c.chatId.userId = ?1 OR c.chatId.userId = ?2 ORDER BY c.chatId.id")
    public List<Chat> getAllChatsForPairOfUsers(Integer userId1, Integer userId2);

    @Query("SELECT c from Chat c WHERE c.chatId.id = ?1")
    public List<Chat> findByChatId(Integer chatId);

//    @Transactional
    @Modifying
    @Query("UPDATE Chat c SET c.chatId.id=?1 WHERE c.chatId.id=?2 AND c.chatId.userId=?3")
    public void updateNewChat(Integer newChatId, Integer oldChatId, Integer userId);
}
