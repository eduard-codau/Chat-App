package paw.project.backend_server.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import paw.project.backend_server.DAO.ChatRepository;
import paw.project.backend_server.DAO.UserProfileRepository;
import paw.project.backend_server.DAO.UserRepository;
import paw.project.backend_server.Model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatService {
    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    private boolean chatExists(Integer chatId)
    {
        List<Chat> existingChat = chatRepository.findByChatId(chatId);

        if(existingChat.size() == 0)
        {
            return false;
        }

        return true;
    }

    private boolean userExists(Integer userId)
    {
        Optional<User> existingUser = userRepository.findById(userId);

        if(existingUser.isEmpty())
        {
            return false;
        }

        return true;
    }

    public ResponseEntity<?> getChatParticipants(Integer chatId)
    {
        if(!chatExists(chatId))
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nu exista conversatia cu id-ul specificat.");
        }

        List<Chat> listOfChats = chatRepository.getAllByChatId(chatId);
        List<UserDTO> listOfParticipants = listOfChats.stream().map((chat) -> { return new UserDTO(chat.getChatId().getUserId());}).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(listOfParticipants);
    }

    public ResponseEntity<?> getAllChatsForUser(Integer userId)
    {
        chatRepository.flush();
        if(!userExists(userId))
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nu exista utilizatorul cu id-ul specificat");
        }

        List<Chat> listOfChats = chatRepository.getAllChatsForUser(userId);
        System.out.println(listOfChats);
        List<Chat> result = new ArrayList<>();
        for(int i=0;i<listOfChats.size();i++)
        {
            List<Chat> listOfChatsByChatId = chatRepository.getAllByChatId(listOfChats.get(i).getChatId().getId());
            System.out.println(listOfChatsByChatId);
            for(int j=0;j<listOfChatsByChatId.size();j++)
            {
                if(listOfChatsByChatId.get(j).getChatId().getUserId() != userId)
                {
                    result.add(listOfChatsByChatId.get(j));
                }
            }
        }

        List<ChatDTO> chatsForUser = new ArrayList<>();

        for(int i=0;i<result.size();i++)
        {
            chatsForUser.add(
                    ChatDTO.builder()
                            .chatId(result.get(i).getChatId().getId())
                            .userId(result.get(i).getChatId().getUserId())
                            .username(userRepository.getById(result.get(i).getChatId().getUserId()).getUsername())
                            .firstName(userProfileRepository.getById(result.get(i).getChatId().getUserId()).getFirstName())
                            .lastName(userProfileRepository.getById(result.get(i).getChatId().getUserId()).getLastName())
                            .build()
            );
        }

        return ResponseEntity.status(HttpStatus.OK).body(chatsForUser);
    }

    public ResponseEntity<?> beginConversation(PairOfUsers pairOfUsers)
    {
        if(!userExists(pairOfUsers.getUserId1()))
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nu exista utilizatorul cu id-ul specificat");
        }

        if(!userExists(pairOfUsers.getUserId2()))
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nu exista utilizatorul cu id-ul specificat");
        }

        List<Chat> chatofUsers = chatRepository.getAllChatsForPairOfUsers(pairOfUsers.getUserId1(), pairOfUsers.getUserId2());//getAllChatsForUser(pairOfUsers.getUserId1());
        for(int i=0;i<chatofUsers.size();i+=2)
        {
            if(i+1 >= chatofUsers.size())
                break;
            if(chatofUsers.get(i).getChatId().getId() == chatofUsers.get(i+1).getChatId().getId())
            {
                if(chatofUsers.get(i).getChatId().getUserId() == pairOfUsers.getUserId1() &&
                        chatofUsers.get(i+1).getChatId().getUserId() == pairOfUsers.getUserId2())
                {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Exista deja o conversatie intre utilizatori");
                }
            }
        }

//        List<Chat> chatofUsers2 = chatRepository.getAllChatsForUser(pairOfUsers.getUserId2());



        ChatId chatIdForUser1 = new ChatId();
        chatIdForUser1.setUserId(pairOfUsers.getUserId1());
//        chatIdForUser1.setId(0);

        Chat chatForUser1 = new Chat();
        chatForUser1.setChatId(chatIdForUser1);

//        System.out.println(chatForUser1.getChatId().getId());
        chatForUser1 = chatRepository.save(chatForUser1);
//        chatRepository.flush();
        System.out.println(chatIdForUser1);

        ChatId chatIdForUser2 = new ChatId();
        chatIdForUser2.setUserId(pairOfUsers.getUserId2());
        chatIdForUser2.setId(chatRepository.getChatByMaxId().getChatId().getId());

        Chat chatForUser2 = new Chat();
        chatForUser2.setChatId(chatIdForUser2);

        chatForUser2 = chatRepository.save(chatForUser2);
//        chatRepository.updateNewChat(chatIdForUser1.getId(),chatIdForUser2.getId(),pairOfUsers.getUserId2());
//        chatRepository.flush();
        return ResponseEntity.status(HttpStatus.CREATED).body("Conversatia a inceput cu succes.");
    }
}
