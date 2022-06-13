package paw.project.backend_server.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import paw.project.backend_server.DAO.ChatRepository;
import paw.project.backend_server.DAO.MessageRepository;
import paw.project.backend_server.DAO.UserRepository;
import paw.project.backend_server.Model.Chat;
import paw.project.backend_server.Model.Message;
import paw.project.backend_server.Model.User;
import paw.project.backend_server.Model.UserDTO;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRepository chatRepository;

    private boolean userExists(Integer userId)
    {
        Optional<User> existingUser = userRepository.findById(userId);

        if(existingUser.isEmpty())
        {
            return false;
        }

        return true;
    }

    private boolean chatExists(Integer chatId)
    {
        List<Chat> existingChat = chatRepository.findByChatId(chatId);

        if(existingChat.size() == 0)
        {
            return false;
        }

        return true;
    }


    private boolean messsageExists(Integer messageId)
    {
        Optional<Message> existingMessage = messageRepository.findById(messageId);

        if(existingMessage.isEmpty())
        {
            return false;
        }

        return true;
    }

    public ResponseEntity<?> getAllMessagesForUser(Integer userId)
    {
        if(!userExists(userId))
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nu exista user-ul cu id-ul specificat.");
        }

        List<Message> allMessagesForUser = messageRepository.getAllMessagesForUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(allMessagesForUser);
    }

    public ResponseEntity<?> getMessagesForUserAndChat(Integer userId, Integer chatId)
    {
        if(!userExists(userId))
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nu exista user-ul cu id-ul specificat.");
        }

        if(!chatExists(chatId))
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nu exista conversatia cu id-ul specificat.");
        }

        List<Message> messagesForUserAndChat = messageRepository.getMessagesForUserAndChat(userId,chatId);

        return ResponseEntity.status(HttpStatus.OK).body(messagesForUserAndChat);
    }

    public ResponseEntity<?> saveMessage(Message message)
    {
        System.out.println(message);
        if(!userExists(message.getFromUser()))
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nu exista user-ul cu id-ul specificat.");
        }

        if(!chatExists(message.getChatId()))
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nu exista conversatia cu id-ul specificat.");
        }

        List<Chat> chatsForUser = chatRepository.getAllChatsForUser(message.getFromUser());
        boolean ok = false;
        for(int i=0;i<chatsForUser.size();i++)
        {
            if(chatsForUser.get(i).getChatId().getId() == message.getChatId())
            {
                ok = true;
            }
        }

        if(!ok)
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Utilizatorul nu apartine acestei conversatii.");
        }

        messageRepository.save(message);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public ResponseEntity<?> deleteMessage(Integer messageId)
    {
        if(!messsageExists(messageId))
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nu exista mesajul cu id-ul specificat.");
        }

        messageRepository.deleteMessageById(messageId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Mesajul a fost sters cu succes");
    }

    public ResponseEntity<?> getConversation(Integer chatId, Integer page, Integer itemsPerPage)
    {
        if(!chatExists(chatId))
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nu exista conversatia cu id-ul specificat.");
        }

        List<Chat> listOfChats = chatRepository.getAllByChatId(chatId);
        List<UserDTO> listOfParticipants = listOfChats.stream().map((chat) -> { return new UserDTO(chat.getChatId().getUserId());})
                .collect(Collectors.toList());

        List<Message> userMessages = new ArrayList<>();
        List<Message> finalUserMessages = userMessages;
        listOfParticipants.stream().forEach((user)-> {
            finalUserMessages.addAll(messageRepository.getMessagesForUserAndChat(user.getUserId(),chatId));
        });
        Collections.sort(userMessages);

        if(page != null)
        {
            if(page <= 0)
            {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pagina nu poate fi negativa");
            }


            if(itemsPerPage != null)
            {
                if(itemsPerPage < 1)
                {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Numarul de intrari de pagina nu poate fi zero sa negativ");
                }

                if((page-1)*itemsPerPage >= userMessages.size())
                {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nu exista pagina cautata");
                }
                int upperLimit;

                if(page*itemsPerPage > userMessages.size())
                {
                    upperLimit = userMessages.size();
                }
                else
                {
                    upperLimit = page*itemsPerPage;
                }
                userMessages = userMessages.subList((page-1)*itemsPerPage,upperLimit);
            }
            else
            {
                if((page-1)*5 >= userMessages.size())
                {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nu exista pagina cautata");
                }

                int upperLimit;

                if(page*5 > userMessages.size())
                {
                    upperLimit = userMessages.size();
                }
                else
                {
                    upperLimit = page*5;
                }

                userMessages = userMessages.subList((page-1)*5,upperLimit);
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(userMessages);
    }
}
