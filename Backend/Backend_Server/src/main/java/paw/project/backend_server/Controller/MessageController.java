package paw.project.backend_server.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import paw.project.backend_server.Model.Message;
import paw.project.backend_server.Service.MessageService;

@RestController
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping("/api/messages/user/{userId}")
    public ResponseEntity<?> getAllMessagesForUser(@PathVariable Integer userId)
    {
        return messageService.getAllMessagesForUser(userId);
    }

    @GetMapping("/api/messages/user/{userId}/chat/{chatId}")
    public ResponseEntity<?> getMessagesForUserAndChat(@PathVariable Integer userId, @PathVariable Integer chatId)
    {
        return messageService.getMessagesForUserAndChat(userId,chatId);
    }

    @PostMapping("/api/messages")
    public ResponseEntity<?> saveMessage(@RequestBody Message message)
    {
        return messageService.saveMessage(message);
    }

    @DeleteMapping("/api/messages/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable Integer messageId)
    {
        return messageService.deleteMessage(messageId);
    }

    @GetMapping("/api/messages/chat/{chatId}")
    public ResponseEntity<?> getConversation(@PathVariable Integer chatId, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer itemsPerPage)
    {
        return messageService.getConversation(chatId,page,itemsPerPage);
    }
}
