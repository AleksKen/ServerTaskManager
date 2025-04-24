package app.code.component;

import app.code.dto.activity.ActivityCreateDTO;
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

import java.util.List;

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

        var names = List.of("feature", "bug");
        for (var name : names) {
            if (labelRepository.findByName(name).isEmpty()) {
                var labelData = new LabelCreateDTO();
                labelData.setName(name);
                var label = labelMapper.map(labelData);
                labelRepository.save(label);
            }
        }

        var activityData = new ActivityCreateDTO();
        activityData.setType("Геометрия");
        var activity = activityMapper.map(activityData);
        activityRepository.save(activity);

        var taskData = new TaskCreateDTO();
        taskData.setTitle("Геометрия");
        taskData.setPriority("high");
        taskData.setStage("todo");

        var task = taskMapper.map(taskData);
        taskRepository.save(task);

    }
}
