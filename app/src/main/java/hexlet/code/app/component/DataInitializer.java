package hexlet.code.app.component;

import hexlet.code.app.dto.TaskStatusCreateDTO;
import hexlet.code.app.dto.UserCreateDTO;
import hexlet.code.app.mapper.TaskStatusMapper;
import hexlet.code.app.mapper.UserMapper;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
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
    private TaskStatusMapper taskStatusMapper;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private final AdminInfo admin;

    @Override
    public void run(ApplicationArguments args) {
        var email = admin.getEmail();
        var password = admin.getPassword();
        var userData = new UserCreateDTO();
        userData.setEmail(email);
        userData.setPassword(password);
        var user = userMapper.map(userData);
        userRepository.save(user);

        var names = List.of("Draft", "ToReview", "ToBeFixed", "ToPublish", "Published");
        var slugs = List.of("draft", "to_review", "to_be_fixed", "to_publish", "published");
        for (var i = 0; i < 5; i++) {
            var statusData = new TaskStatusCreateDTO();
            var status = new TaskStatus();
            statusData.setName(names.get(i));
            statusData.setSlug(slugs.get(i));
            status = taskStatusMapper.map(statusData);
            taskStatusRepository.save(status);
        }
    }
}
