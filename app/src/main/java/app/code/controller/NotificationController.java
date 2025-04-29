package app.code.controller;

import app.code.dto.notification.NotificationCreateDTO;
import app.code.dto.notification.NotificationDTO;
import app.code.dto.notification.NotificationUpdateDTO;
import app.code.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RestController
@RequestMapping(path = "/api/notifications")
@CrossOrigin(origins = "http://localhost:3000")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationDTO>> index() {
        var res = notificationService.index();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(res.size()))
                .body(res);
    }

    @GetMapping(path = "/{id}")
    public NotificationDTO show(@PathVariable Long id) {
        return notificationService.show(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NotificationDTO create(@Valid @RequestBody NotificationCreateDTO dto) {
        return notificationService.create(dto);
    }

    @PutMapping(path = "/{id}")
    public NotificationDTO update(@PathVariable Long id, @Valid @RequestBody NotificationUpdateDTO dto) {
        return notificationService.update(id, dto);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        notificationService.destroy(id);
    }
}
