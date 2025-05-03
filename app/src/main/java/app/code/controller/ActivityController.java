package app.code.controller;

import app.code.dto.activity.ActivityCreateDTO;
import app.code.dto.activity.ActivityDTO;
import app.code.dto.activity.ActivityUpdateDTO;
import app.code.service.ActivityService;
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
@RequestMapping(path = "/api/activities")
@CrossOrigin(origins = "https://mariatask.netlify.app")
public class ActivityController {
    @Autowired
    private ActivityService activityService;

    @GetMapping
    public ResponseEntity<List<ActivityDTO>> index() {
        var res = activityService.index();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(res.size()))
                .body(res);
    }

    @GetMapping(path = "/{id}")
    public ActivityDTO show(@PathVariable Long id) {
        return activityService.show(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ActivityDTO create(@Valid @RequestBody ActivityCreateDTO dto) {
        return activityService.create(dto);
    }

    @PutMapping(path = "/{id}")
    public ActivityDTO update(@PathVariable Long id, @Valid @RequestBody ActivityUpdateDTO dto) {
        return activityService.update(id, dto);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        activityService.destroy(id);
    }
}
