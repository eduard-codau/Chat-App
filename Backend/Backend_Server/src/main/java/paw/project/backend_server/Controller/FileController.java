package paw.project.backend_server.Controller;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import paw.project.backend_server.Service.FileService;

import java.util.HashMap;
import java.util.Map;

@RestController
public class FileController {

    @Autowired
    FileService fileService;

    @PostMapping("/api/image/upload-picture/{userId}")
    public ResponseEntity<?> postImage(@RequestParam("imageFile") MultipartFile file, @PathVariable Integer userId)
    {
        try {
            fileService.saveFile("User_" + userId + "." + file.getOriginalFilename().split("\\.", 2)[1], file.getBytes());
            return ResponseEntity.ok().build();
        }
        catch(Exception e)
        {
            System.out.println(e);
            return ResponseEntity.badRequest().build();
        }
    }
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/api/image/get-picture/{userId}")
    public ResponseEntity<?> getImage( @PathVariable Integer userId)
    {
        try {
            byte[] file = fileService.getImage(userId);
            Map<String, Object> parameters = new HashMap<>();

            parameters.put("picByte", file);

            return ResponseEntity.ok().body(parameters);
        }
        catch(Exception e)
        {
            System.out.println(e);
            return ResponseEntity.badRequest().build();
        }
    }

}
