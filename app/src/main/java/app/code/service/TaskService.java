package app.code.service;

import app.code.dto.TaskParamDTO;
import app.code.dto.task.TaskCreateDTO;
import app.code.dto.task.TaskDTO;
import app.code.dto.task.TaskUpdateDTO;
import app.code.exception.ResourceNotFoundException;
import app.code.mapper.TaskMapper;
import app.code.repository.TaskRepository;
import app.code.specification.TaskSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskSpecification specBuilder;

    public List<TaskDTO> index(TaskParamDTO params) {
        var spec = specBuilder.build(params);
        return taskRepository.findAll(spec).stream().map(taskMapper::map).toList();
    }

    public TaskDTO show(Long id) {
        return taskMapper.map(taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found!")));
    }

    public TaskDTO create(TaskCreateDTO dto) {
        var task = taskMapper.map(dto);
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    public TaskDTO update(Long id, TaskUpdateDTO dto) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found!"));
        taskMapper.update(dto, task);
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    public void destroy(Long id) {
        taskRepository.deleteById(id);
    }
}
