package com.unihub.unihubproxyserver.Services;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class UnihubGatewayService {
    private final RestTemplate restTemplate;

    public UnihubGatewayService(WebClient.Builder webClientBuilder) {
        String GATEWAY_IP = "http://localhost";
        int GATEWAY_PORT = 8081;
        String url = GATEWAY_IP + ":" + GATEWAY_PORT;

        this.restTemplate = new RestTemplate();
    }

    public ResponseEntity<Object> getConversationsFromGateway(HttpHeaders headers, Integer userId) {
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        return restTemplate.exchange(String.format("http://localhost:8081/api/chat/all-chats/%d", userId), HttpMethod.GET, entity, Object.class);
    }

    public ResponseEntity<Object> getConversationsInfos(HttpHeaders headers, Integer userId) {
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        return restTemplate.exchange(String.format("http://localhost:8081/api/chat/all-chats/%d", userId), HttpMethod.GET, entity, Object.class);
    }

    public ResponseEntity<Object> registerUser(Object postBody) {
        return restTemplate.postForEntity("http://localhost:8081/api/auth/register", postBody, Object.class);
    }

    public ResponseEntity<Object> loginUser(Object postBody) {
        return restTemplate.postForEntity("http://localhost:8081/api/auth/login/", postBody, Object.class);
    }

    public ResponseEntity<Object> getUserProfile(HttpHeaders headers, Integer userId) {
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        return restTemplate.exchange(String.format("http://localhost:8081/api/users/profiles/%d", userId), HttpMethod.GET, entity, Object.class);
    }

    public ResponseEntity<Object> uploadProfilePicture(HttpHeaders headers, MultipartFile file, Integer userId) throws IOException {

        String[] splitted = file.getOriginalFilename().split("\\.", 2);

        Path tempFile = Files.createTempFile(splitted[0], "." + splitted[1]);

        Files.write(tempFile, file.getBytes());
        File fileToSend = tempFile.toFile();

        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();

        parameters.add("imageFile", new FileSystemResource(fileToSend));


        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(parameters, headers);


        ResponseEntity<Object> re = restTemplate.exchange(String.format("http://localhost:8081/api/image/upload-picture/%d", userId), HttpMethod.POST,
                httpEntity, Object.class);

        fileToSend.delete();


        return re;

    }

    public ResponseEntity<Object> getUsersByQuery(String query, HttpHeaders headers){
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        return restTemplate.exchange(String.format("http://localhost:8081/api/users/search?query=%s", query), HttpMethod.GET, entity, Object.class);
    }

    public ResponseEntity<Object> getSuggestedFriends(Integer userId, HttpHeaders headers){
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        return restTemplate.exchange(String.format("http://localhost:8081/api/suggestion/%d", userId), HttpMethod.GET, entity, Object.class);
    }

    public ResponseEntity<Object> validateToken(HttpHeaders headers){
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        return restTemplate.exchange("http://localhost:8081/api/auth/validate-token", HttpMethod.GET, entity, Object.class);
    }

    public ResponseEntity<Object> updateProfile(HttpHeaders headers, Object body){
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<Object> entity = new HttpEntity<Object>(body, headers);
        return restTemplate.exchange("http://localhost:8081/api/users/profiles", HttpMethod.PUT, entity, Object.class);
    }

   public ResponseEntity<Object> getConversation(HttpHeaders headers, Integer chatId, Integer page, Integer itemsPerPage){
       RestTemplate restTemplate = new RestTemplate();

       HttpEntity<String> entity = new HttpEntity<>("body", headers);
       return restTemplate.exchange(String.format("http://localhost:8081/api/messages/chat/%d?page=%d&itemsPerPage=%d", chatId, page, itemsPerPage), HttpMethod.GET, entity, Object.class);
   }

    public ResponseEntity<Object> sendFriendRequest(HttpHeaders headers, Object body){
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<Object> entity = new HttpEntity<Object>(body, headers);
        return restTemplate.exchange("http://localhost:8081/api/friends/addfriend", HttpMethod.POST, entity, Object.class);
    }

    public ResponseEntity<Object> getFriendRequests(HttpHeaders headers, Integer userId){
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        return restTemplate.exchange(String.format("http://localhost:8081/api/friends/friendrequests/%d", userId), HttpMethod.GET, entity, Object.class);
    }
    public ResponseEntity<Object> acceptFriendRequest(HttpHeaders headers, Object body){
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<Object> entity = new HttpEntity<Object>(body, headers);
        return restTemplate.exchange("http://localhost:8081/api/friends/acceptrequest", HttpMethod.POST, entity, Object.class);
    }

    public ResponseEntity<Object> deleteFriendRequest(HttpHeaders headers, Object body){
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<Object> entity = new HttpEntity<Object>(body, headers);
        return restTemplate.exchange("http://localhost:8081/api/friends/deleterequest", HttpMethod.POST, entity, Object.class);
    }


}
