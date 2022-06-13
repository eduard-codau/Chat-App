package paw.project.backend_server.Controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@RestController
public class AuthController {

    @PostMapping("/api/auth/register")
    public ResponseEntity<?> registerUser(@RequestBody Object body)
    {
        try {

            String url = "http://localhost:8083/api/user/register";
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Object> rateResponse =
                    restTemplate.postForEntity(url, body, Object.class);


            return ResponseEntity.ok().headers(rateResponse.getHeaders()).body(rateResponse.getBody());

        }catch (HttpStatusCodeException httpStatusCodeException){
            System.out.println(httpStatusCodeException);
            return ResponseEntity.badRequest().body(httpStatusCodeException.getResponseBodyAsString());
        }

    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<?> loginUser(@RequestBody Object body)
    {
        try {

            String url = "http://localhost:8083/api/user/login";
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Object> rateResponse =
                    restTemplate.postForEntity(url, body, Object.class);


            return ResponseEntity.ok().headers(rateResponse.getHeaders()).body(rateResponse.getBody());

        }catch (HttpStatusCodeException httpStatusCodeException){
            System.out.println(httpStatusCodeException);
            return ResponseEntity.badRequest().body(httpStatusCodeException.getResponseBodyAsString());
        }
    }

    @GetMapping("/api/auth/validate-token")
    public ResponseEntity<?> validateJwt( @RequestHeader HttpHeaders headers)
    {
        try {
            String url = "http://localhost:8083/api/user/validate-token";
            HttpEntity<String> entity = new HttpEntity<>("body", headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Object> rateResponse =
                    restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);
            System.out.println(rateResponse.getBody());
            return ResponseEntity.ok().headers(rateResponse.getHeaders()).body(rateResponse.getBody());

        }catch (HttpStatusCodeException httpStatusCodeException){
            return ResponseEntity.badRequest().body(httpStatusCodeException.getResponseBodyAsString());
        }
    }
}

