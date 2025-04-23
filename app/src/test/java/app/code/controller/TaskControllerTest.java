package app.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import app.code.dto.TaskParamDTO;
import app.code.exception.ResourceNotFoundException;
import app.code.mapper.TaskMapper;
import app.code.mapper.UserMapper;
import app.code.model.Label;
import app.code.model.Task;
import app.code.model.TaskStatus;
import app.code.repository.LabelRepository;
import app.code.repository.TaskRepository;
import app.code.repository.TaskStatusRepository;
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
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ModelGenerator modelGenerator;

    private Task testTask;
    private TaskStatus testTaskStatus;
    private Label testLabel;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(springSecurity())
                .build();
        testTask = Instancio.of(modelGenerator.getTaskModel())
                .create();

        testTaskStatus = Instancio.of(modelGenerator.getTaskStatusModel())
                .create();
        taskStatusRepository.save(testTaskStatus);
        testTask.setTaskStatus(testTaskStatus);

        testLabel = Instancio.of(modelGenerator.getLabelModel())
                .create();
        labelRepository.save(testLabel);
        testTask.setLabels(Set.of(testLabel));

        testTask.setAssignee(userRepository.findByEmail("hexlet@example.com").get());
    }

    @AfterEach
    public void clean() {
        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();
        labelRepository.deleteAll();
    }

    @Test
    public void testIndex() throws Exception {
        taskRepository.save(testTask);
        var result = mockMvc.perform(get("/api/tasks").with(jwt()))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testIndexWithParams() throws Exception {
        var params = new TaskParamDTO();
        params.setAssigneeId(userRepository.findByEmail("hexlet@example.com").get().getId());
        params.setStatus(testTaskStatus.getSlug());
        params.setLabelId(testLabel.getId());
        params.setTitleCont(testTask.getName().substring(3).toUpperCase());
        taskRepository.save(testTask);
        var result = mockMvc.perform(get("/api/tasks?titleCont=" + params.getTitleCont()
                        + "&assigneeId=" + params.getAssigneeId()
                        + "&status=" + params.getStatus()
                        + "&labelId=" + params.getLabelId()).with(jwt()))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray().element(0).and(
                v -> v.node("title").isEqualTo(testTask.getName()),
                v -> v.node("status").isEqualTo(testTask.getTaskStatus().getSlug())
        );
    }

    @Test
    public void testShow() throws Exception {
        taskRepository.save(testTask);
        var task = taskRepository.findById(testTask.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found!"));
        var request = get("/api/tasks/{id}", task.getId()).with(jwt());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("title").isEqualTo(testTask.getName()),
                v -> v.node("status").isEqualTo(testTask.getTaskStatus().getSlug())
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
        assertThat(task.getTaskStatus().getSlug()).isEqualTo(testTask.getTaskStatus().getSlug());
    }

    @Test
    public void testUpdate() throws Exception {
        taskRepository.save(testTask);
        var title = faker.hearthstone().mainCharacter();
        var data = new HashMap<>();
        data.put("title", title);

        var request = put("/api/tasks/{id}", testTask.getId()).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        var result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("title").isEqualTo(title)
        );
    }

    @Test
    public void testDestroy() throws Exception {
        taskRepository.save(testTask);
        var id = testTask.getId();
        var request = delete("/api/tasks/{id}", id).with(jwt());
        mockMvc.perform(request).andExpect(status().isNoContent());
        assertThat(taskRepository.findById(id)).isEmpty();
    }
}
