package paw.project.backend_server.Controller;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import paw.project.backend_server.Model.User;
import paw.project.backend_server.Model.UserProfile;
import paw.project.backend_server.Service.SuggestionService;

@RestController
public class SuggestionController {

    @Autowired
    private SuggestionService suggestionService;

    @GetMapping("/api/suggestion/{id}")
    public ResponseEntity<?> getSuggestionByFaculty(@PathVariable Integer id) throws JSONException {

        return suggestionService.getSuggestionByFaculty(id);
    }
}
