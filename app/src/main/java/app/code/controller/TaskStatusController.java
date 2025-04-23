package app.code.controller;

import app.code.dto.taskStatus.TaskStatusCreateDTO;
import app.code.dto.taskStatus.TaskStatusDTO;
import app.code.dto.taskStatus.TaskStatusUpdateDTO;
import app.code.service.TaskStatusService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
@RequestMapping(path = "/api/task_statuses")
public class TaskStatusController {
    @Autowired
    private TaskStatusService taskStatusService;

    @GetMapping
    public ResponseEntity<List<TaskStatusDTO>> index() {
        var res = taskStatusService.index();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(res.size()))
                .body(res);
    }

    @GetMapping(path = "/{id}")
    public TaskStatusDTO show(@PathVariable Long id) {
        return taskStatusService.show(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskStatusDTO create(@Valid @RequestBody TaskStatusCreateDTO dto) {
        return taskStatusService.create(dto);
    }

    @PutMapping(path = "/{id}")
    public TaskStatusDTO update(@PathVariable Long id, @Valid @RequestBody TaskStatusUpdateDTO dto) {
        return taskStatusService.update(id, dto);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        taskStatusService.destroy(id);
    }
}
