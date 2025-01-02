package hexlet.code.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.exception.ResourceNotFoundException;
import hexlet.code.app.mapper.TaskStatusMapper;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.util.ModelGenerator;
import net.datafaker.Faker;
import org.assertj.core.api.Assertions;
import org.instancio.Instancio;
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

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskStatusControllerTest {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusMapper taskStatusMapper;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ModelGenerator modelGenerator;

    private TaskStatus testTaskStatus;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(springSecurity())
                .build();
        testTaskStatus = Instancio.of(modelGenerator.getTaskStatusModel())
                .create();
    }

    @Test
    public void testIndex() throws Exception {
        taskStatusRepository.save(testTaskStatus);
        var result = mockMvc.perform(get("/api/task_statuses").with(jwt()))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        taskStatusRepository.save(testTaskStatus);
        var status = taskStatusRepository.findBySlug(testTaskStatus.getSlug())
                .orElseThrow(() -> new ResourceNotFoundException("Status not found!"));
        var request = get("/api/task_statuses/{id}", status.getId()).with(jwt());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(testTaskStatus.getName()),
                v -> v.node("slug").isEqualTo(testTaskStatus.getSlug())
        );
    }

    @Test
    public void testCreate() throws Exception {
        var request = post("/api/task_statuses").with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(testTaskStatus));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var status = taskStatusRepository.findBySlug(testTaskStatus.getSlug()).get();
        assertThat(status.getName()).isEqualTo(testTaskStatus.getName());
        assertThat(status.getSlug()).isEqualTo(testTaskStatus.getSlug());
    }

    @Test
    public void testUpdate() throws Exception {
        taskStatusRepository.save(testTaskStatus);
        var name = faker.hearthstone().mainCharacter();
        var slug = faker.zodiac().sign();
        var data = new HashMap<>();
        data.put("name", name);
        data.put("slug", slug);

        var status = taskStatusRepository.findBySlug(testTaskStatus.getSlug())
                .orElseThrow(() -> new ResourceNotFoundException("Status not found!"));

        var request = put("/api/task_statuses/{id}", status.getId()).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request).andExpect(status().isOk());

        status = taskStatusRepository.findBySlug(slug).get();
        assertThat(status.getName()).isEqualTo(name);
        assertThat(status.getSlug()).isEqualTo(slug);
    }

    @Test
    public void testUpdateWithoutAuth() throws Exception {
        taskStatusRepository.save(testTaskStatus);
        var name = faker.hearthstone().mainCharacter();
        var slug = faker.zodiac().sign();
        var data = new HashMap<>();
        data.put("name", name);
        data.put("slug", slug);

        var status = taskStatusRepository.findBySlug(testTaskStatus.getSlug())
                .orElseThrow(() -> new ResourceNotFoundException("Status not found!"));

        var request = put("/api/task_statuses/{id}", status.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request).andExpect(status().isUnauthorized());
    }

    @Test
    public void testUpdateEmptyName() throws Exception {
        taskStatusRepository.save(testTaskStatus);
        var slug = faker.zodiac().sign();
        var data = new HashMap<>();
        data.put("name", "");
        data.put("slug", slug);

        var status = taskStatusRepository.findBySlug(testTaskStatus.getSlug())
                .orElseThrow(() -> new ResourceNotFoundException("Status not found!"));

        var request = put("/api/task_statuses/{id}", status.getId()).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request).andExpect(status().isBadRequest());
    }

    @Test
    public void testDestroy() throws Exception {
        taskStatusRepository.save(testTaskStatus);
        var status = taskStatusRepository.findBySlug(testTaskStatus.getSlug())
                .orElseThrow(() -> new ResourceNotFoundException("Status not found!"));

        var request = delete("/api/task_statuses/{id}", status.getId()).with(jwt());
        mockMvc.perform(request).andExpect(status().isNoContent());

        Assertions.assertThat(taskStatusRepository.findBySlug(testTaskStatus.getSlug())).isEmpty();
    }
}
