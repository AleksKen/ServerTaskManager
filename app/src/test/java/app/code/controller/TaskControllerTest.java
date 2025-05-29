package app.code.controller;

import app.code.model.Activity;
import app.code.repository.ActivityRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import app.code.exception.ResourceNotFoundException;
import app.code.mapper.TaskMapper;
import app.code.model.Label;
import app.code.model.Task;
import app.code.repository.LabelRepository;
import app.code.repository.TaskRepository;
import app.code.repository.UserRepository;
import app.code.util.ModelGenerator;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Set;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ModelGenerator modelGenerator;

    private Task testTask;
    private Label testLabel;
    private Activity testActivity;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(springSecurity())
                .build();
        testTask = Instancio.of(modelGenerator.getTaskModel())
                .create();

        testLabel = Instancio.of(modelGenerator.getLabelModel())
                .create();

        testActivity = Instancio.of(modelGenerator.getActivityModel())
                .create();

        labelRepository.save(testLabel);
        testTask.setLabels(Set.of(testLabel));
        testActivity.setTask(testTask);
        testTask.setActivities(Set.of(testActivity));
        testTask.setTeam(Set.of(userRepository.findByEmail("maria@example.com").get()));
    }

    @AfterEach
    public void clean() {
        taskRepository.deleteAll();
        activityRepository.deleteAll();
        labelRepository.deleteAll();
    }

    @Test
    public void testIndex() throws Exception {
        taskRepository.save(testTask);
        activityRepository.save(testActivity);
        var result = mockMvc.perform(get("/api/tasks").with(jwt()))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        taskRepository.save(testTask);
        activityRepository.save(testActivity);
        var task = taskRepository.findById(testTask.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found!"));
        var request = get("/api/tasks/{id}", task.getId()).with(jwt());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("title").isEqualTo(testTask.getName()),
                v -> v.node("team").isArray(),
                v -> v.node("activities").isArray(),
                v -> v.node("labels").isArray()
        );
    }

    @Test
    public void testCreate() throws Exception {
        var taskCreate = taskMapper.map(testTask);
        var request = post("/api/tasks").with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(taskCreate));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var task = taskRepository.findByName(testTask.getName()).get();
        assertThat(task.getName()).isEqualTo(testTask.getName());
    }

    @Test
    public void testUpdate() throws Exception {
        taskRepository.save(testTask);
        activityRepository.save(testActivity);
        var title = faker.hearthstone().mainCharacter();
        var data = new HashMap<>();
        data.put("title", title);

        var request = put("/api/tasks/{id}", testTask.getId()).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        var result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("title").isEqualTo(title),
                v -> v.node("team").isArray(),
                v -> v.node("activities").isArray(),
                v -> v.node("labels").isArray()
        );
    }

    @Test
    public void testDestroy() throws Exception {
        taskRepository.save(testTask);
        activityRepository.save(testActivity);
        var id = testTask.getId();
        var request = delete("/api/tasks/{id}", id).with(jwt());
        mockMvc.perform(request).andExpect(status().isNoContent());
        assertThat(taskRepository.findById(id)).isEmpty();
    }
}
