package com.unihub.unihubproxyserver.Controller;


import com.unihub.unihubproxyserver.Services.UnihubGatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.util.Map;

@RestController
public class UnihubController {

    @Autowired
    UnihubGatewayService gateway;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/conversations/{userId}")
    ResponseEntity<Object> getConversations( @RequestHeader HttpHeaders headers,@PathVariable Integer userId )
    {
        try {

            return gateway.getConversationsFromGateway(headers, userId);

        }catch (HttpStatusCodeException httpStatusCodeException){
            return ResponseEntity.badRequest().body(httpStatusCodeException.getResponseBodyAsString());
        }
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/register-user")
    ResponseEntity<Object> registerUser(@RequestBody Map<String, Object> payload)
    {
        try {

            return gateway.registerUser(payload);

        }catch (HttpStatusCodeException httpStatusCodeException){
            return ResponseEntity.badRequest().body(httpStatusCodeException.getResponseBodyAsString());
        }
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/login-user")
    ResponseEntity<Object> loginUser(@RequestBody Map<String, Object> payload) {
        try {
            return gateway.loginUser(payload);

        }catch (HttpStatusCodeException httpStatusCodeException){
            System.out.println(httpStatusCodeException);
            return ResponseEntity.badRequest().body(httpStatusCodeException.getResponseBodyAsString());
        }
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/get-profile/{userId}")
    ResponseEntity<Object> loginUser( @RequestHeader HttpHeaders headers, @PathVariable Integer userId) {
        try {
            return gateway.getUserProfile(headers, userId);

        }catch (HttpStatusCodeException httpStatusCodeException){
            return ResponseEntity.badRequest().body(httpStatusCodeException.getResponseBodyAsString());
        }
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/profile-image/upload/{userId}")
    public ResponseEntity<Object> uploadImage(@RequestHeader HttpHeaders headers, @RequestParam("imageFile") MultipartFile file, @PathVariable Integer userId) throws IOException {
        try {
            return gateway.uploadProfilePicture(headers, file, userId);

        }catch (HttpStatusCodeException httpStatusCodeException){
            return ResponseEntity.badRequest().body(httpStatusCodeException.getResponseBodyAsString());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }


    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/search-friends")
    ResponseEntity<Object> searchForFriends( @RequestHeader HttpHeaders headers, @RequestParam String query )
    {
        try {

            return gateway.getUsersByQuery(query, headers);

        }catch (HttpStatusCodeException httpStatusCodeException){
            return ResponseEntity.badRequest().body(httpStatusCodeException.getResponseBodyAsString());
        }
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/suggest-friends/{userId}")
    ResponseEntity<Object> suggestFriends( @RequestHeader HttpHeaders headers,@PathVariable Integer userId )
    {
        try {

            return gateway.getSuggestedFriends(userId, headers);

        }catch (HttpStatusCodeException httpStatusCodeException){
            return ResponseEntity.badRequest().body(httpStatusCodeException.getResponseBodyAsString());
        }
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/validate-token")
    ResponseEntity<Object> validateToken( @RequestHeader HttpHeaders headers)
    {
        try {

            return gateway.validateToken(headers);

        }catch (HttpStatusCodeException httpStatusCodeException){
            return ResponseEntity.badRequest().body(httpStatusCodeException.getResponseBodyAsString());
        }
    }


    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/update-profile")
    ResponseEntity<Object> updateUser( @RequestHeader HttpHeaders headers, @RequestBody Map<String, Object> payload)
    {
        try {
            return gateway.updateProfile(headers, payload);

        }catch (HttpStatusCodeException httpStatusCodeException){
            return ResponseEntity.badRequest().body(httpStatusCodeException.getResponseBodyAsString());
        }
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/chat-informations/{userId}")
    ResponseEntity<Object> updateUser( @RequestHeader HttpHeaders headers,@PathVariable Integer userId )
    {
        try {
            return gateway.getConversationsInfos(headers, userId);

        }catch (HttpStatusCodeException httpStatusCodeException){
            return ResponseEntity.badRequest().body(httpStatusCodeException.getResponseBodyAsString());
        }
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/get-messages/{chatId}")
    public ResponseEntity<?> getConversation( @RequestHeader HttpHeaders headers, @PathVariable Integer chatId, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer itemsPerPage)
    {
        try {
            return gateway.getConversation(headers, chatId,page,itemsPerPage);

        }catch (HttpStatusCodeException httpStatusCodeException){
            return ResponseEntity.badRequest().body(httpStatusCodeException.getResponseBodyAsString());
        }
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/send-request")
    ResponseEntity<Object> sendRequest( @RequestHeader HttpHeaders headers, @RequestBody Map<String, Object> payload)
    {
        try {
            return gateway.sendFriendRequest(headers, payload);

        }catch (HttpStatusCodeException httpStatusCodeException){
            return ResponseEntity.badRequest().body(httpStatusCodeException.getResponseBodyAsString());
        }
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/get-requests/{userId}")
    public ResponseEntity<?> getRequests( @RequestHeader HttpHeaders headers, @PathVariable Integer userId)
    {
        try {
            return gateway.getFriendRequests(headers, userId);
        }catch (HttpStatusCodeException httpStatusCodeException){
            return ResponseEntity.badRequest().body(httpStatusCodeException.getResponseBodyAsString());
        }
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/accept-request")
    public ResponseEntity<?> acceptRequest( @RequestHeader HttpHeaders headers, @RequestBody Map<String, Object> payload)
    {
        try {
            return gateway.acceptFriendRequest(headers, payload);
        }catch (HttpStatusCodeException httpStatusCodeException){
            return ResponseEntity.badRequest().body(httpStatusCodeException.getResponseBodyAsString());
        }
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/delete-request")
    public ResponseEntity<?> deleteRequest( @RequestHeader HttpHeaders headers, @RequestBody Map<String, Object> payload)
    {
        try {
            return gateway.deleteFriendRequest(headers, payload);
        }catch (HttpStatusCodeException httpStatusCodeException){
            return ResponseEntity.badRequest().body(httpStatusCodeException.getResponseBodyAsString());
        }
    }
}