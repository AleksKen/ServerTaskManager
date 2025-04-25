package app.code.component;

import app.code.dto.label.LabelCreateDTO;
import app.code.dto.task.TaskCreateDTO;
import app.code.dto.user.UserCreateDTO;
import app.code.mapper.ActivityMapper;
import app.code.mapper.LabelMapper;
import app.code.mapper.TaskMapper;
import app.code.mapper.UserMapper;
import app.code.repository.ActivityRepository;
import app.code.repository.LabelRepository;
import app.code.repository.TaskRepository;
import app.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabelMapper labelMapper;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private ActivityRepository activityRepository;

    @Override
    public void run(ApplicationArguments args) {
        var email = "hexlet@example.com";
        var password = "qwerty";
        if (userRepository.findByEmail(email).isEmpty()) {
            var userData = new UserCreateDTO();
            userData.setEmail(email);
            userData.setPassword(password);
            userData.setFirstName("Maria");
            userData.setLastName("Konyashova");
            userData.setIsActive(true);
            userData.setIsAdmin(true);

            var user = userMapper.map(userData);
            userRepository.save(user);
        }

        var names = List.of("алгебра", "геометрия");
        for (var name : names) {
            if (labelRepository.findByName(name).isEmpty()) {
                var labelData = new LabelCreateDTO();
                labelData.setName(name);
                var label = labelMapper.map(labelData);
                labelRepository.save(label);
            }
        }


        var taskData = new TaskCreateDTO();
        taskData.setTitle("ДЗ треугольники и уравнения");
        taskData.setPriority("high");
        taskData.setStage("todo");
        taskData.setDeadline(Instant.now());
        taskData.setTeamIds(Set.of(1L));
        taskData.setTaskLabelIds(Set.of(1L, 2L));

        var task = taskMapper.map(taskData);
        taskRepository.save(task);

    }
}
