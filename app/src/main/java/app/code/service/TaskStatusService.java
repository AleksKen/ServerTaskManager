package app.code.service;

import app.code.dto.taskStatus.TaskStatusCreateDTO;
import app.code.dto.taskStatus.TaskStatusDTO;
import app.code.dto.taskStatus.TaskStatusUpdateDTO;
import app.code.exception.ResourceNotFoundException;
import app.code.mapper.TaskStatusMapper;
import app.code.repository.TaskStatusRepository;
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
