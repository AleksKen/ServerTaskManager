package hexlet.code.app.component;

import hexlet.code.app.dto.UserCreateDTO;
import hexlet.code.app.mapper.UserMapper;
import hexlet.code.app.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

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
    }
}
