package app.code.util;

import app.code.model.Label;
import app.code.model.Task;
import app.code.model.User;
import app.code.model.Activity;
import app.code.model.Notification;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ModelGenerator {
    private Model<User> userModel;
    private Model<Task> taskModel;
    private Model<Label> labelModel;
    private Model<Activity> activityModel;
    private Model<Notification> notificationModel;

    @Autowired
    private Faker faker;

    @PostConstruct
    private void init() {
        userModel = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .ignore(Select.field(User::getCreatedAt))
                .ignore(Select.field(User::getUpdatedAt))
                .supply(Select.field(User::getFirstName), () -> faker.animal().name())
                .supply(Select.field(User::getLastName), () -> faker.gameOfThrones().dragon())
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .supply(Select.field(User::getPassword), () -> faker.kpop().girlGroups())
                .toModel();


        taskModel = Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .ignore(Select.field(Task::getTeam))
                .ignore(Select.field(Task::getCreatedAt))
                .ignore(Select.field(Task::getLabels))
                .ignore(Select.field(Task::getActivities))
                .supply(Select.field(Task::getName), () -> faker.gameOfThrones().dragon())
                .toModel();


        labelModel = Instancio.of(Label.class)
                .ignore(Select.field(Label::getId))
                .ignore(Select.field(Label::getTasks))
                .ignore(Select.field(Label::getCreatedAt))
                .supply(Select.field(Label::getName), () -> faker.brand().car())
                .toModel();

        activityModel = Instancio.of(Activity.class)
                .ignore(Select.field(Activity::getId))
                .ignore(Select.field(Activity::getCreatedAt))
                .ignore(Select.field(Activity::getTask))
                .supply(Select.field(Activity::getType), () -> faker.brand().car())
                .supply(Select.field(Activity::getContent), () -> faker.kpop().girlGroups())
                .toModel();

        notificationModel = Instancio.of(Notification.class)
                .ignore(Select.field(Notification::getId))
                .ignore(Select.field(Notification::getCreatedAt))
                .ignore(Select.field(Notification::getTeam))
                .ignore(Select.field(Notification::getTaskId))
                .supply(Select.field(Notification::getType), () -> faker.business().creditCardExpiry())
                .supply(Select.field(Notification::getText), () -> faker.app().name())
                .toModel();
    }
}
