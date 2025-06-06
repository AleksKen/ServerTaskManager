package app.code.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class WelcomeController {
    @GetMapping(path = "/welcome")
    public String index() {
        return "Welcome to Spring";
    }
}
