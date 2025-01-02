package hexlet.code.app.service;

import hexlet.code.app.dto.taskStatus.TaskStatusCreateDTO;
import hexlet.code.app.dto.taskStatus.TaskStatusDTO;
import hexlet.code.app.dto.taskStatus.TaskStatusUpdateDTO;
import hexlet.code.app.exception.ResourceNotFoundException;
import hexlet.code.app.mapper.TaskStatusMapper;
import hexlet.code.app.repository.TaskStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskStatusService {
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskStatusMapper taskStatusMapper;

    public List<TaskStatusDTO> index() {
        return taskStatusRepository.findAll().stream().map(taskStatusMapper::map).toList();
    }

    public TaskStatusDTO show(Long id) {
        return taskStatusMapper.map(taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Status with id " + id + " not found!")));
    }

    public TaskStatusDTO create(TaskStatusCreateDTO dto) {
        var status = taskStatusMapper.map(dto);
        taskStatusRepository.save(status);
        return taskStatusMapper.map(status);
    }

    public TaskStatusDTO update(Long id, TaskStatusUpdateDTO dto) {
        var status = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Status with id " + id + " not found!"));
        taskStatusMapper.update(dto, status);
        taskStatusRepository.save(status);
        return taskStatusMapper.map(status);
    }

    public void destroy(Long id) {
        taskStatusRepository.deleteById(id);
    }
}
