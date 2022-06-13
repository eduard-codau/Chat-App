package paw.project.backend_server.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import paw.project.backend_server.Model.Message;

import javax.transaction.Transactional;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    @Query("SELECT m from Message m WHERE m.fromUser = ?1")
    public List<Message> getAllMessagesForUser(Integer userId);

    @Query("SELECT m from Message m WHERE m.fromUser = ?1 AND m.chatId = ?2")
    public List<Message> getMessagesForUserAndChat(Integer userId, Integer chatId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Message m WHERE m.messageId = ?1")
    public void deleteMessageById(Integer messageId);
}
