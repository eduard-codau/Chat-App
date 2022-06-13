package paw.project.backend_server.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import paw.project.backend_server.Model.PairOfUsers;
import paw.project.backend_server.Service.ChatService;

@RestController
public class ChatController {
    @Autowired
    private ChatService chatService;

    @GetMapping("/api/chat/{chatId}")
    public ResponseEntity<?> getChatParticipants(@PathVariable Integer chatId)
    {
        return chatService.getChatParticipants(chatId);
    }

    @GetMapping("/api/chat/all-chats/{userId}")
    public ResponseEntity<?> getAllChatsForUser(@PathVariable Integer userId)
    {
        return chatService.getAllChatsForUser(userId);
    }

    @PostMapping("/api/chat/start")
    public ResponseEntity<?> beginConversation(@RequestBody PairOfUsers pairOfUsers)
    {
        return chatService.beginConversation(pairOfUsers);
    }
}
