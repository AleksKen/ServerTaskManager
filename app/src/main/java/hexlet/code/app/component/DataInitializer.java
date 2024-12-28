package hexlet.code.app.component;

import hexlet.code.app.model.User;
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
    private final UserRepository userRepository;

    @Autowired
    private final AdminInfo admin;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var email = admin.getEmail();
        var password = admin.getPassword();
        var userData = new User();
        userData.setEmail(email);
        userData.setPassword(password);
        userRepository.save(userData);
    }
}
