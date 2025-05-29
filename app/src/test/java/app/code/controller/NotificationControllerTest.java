package app.code.controller;

import app.code.mapper.NotificationMapper;
import app.code.model.Notification;
import app.code.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import app.code.exception.ResourceNotFoundException;
import app.code.util.ModelGenerator;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class NotificationControllerTest {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ModelGenerator modelGenerator;

    private Notification testNotification;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(springSecurity())
                .build();
        testNotification = Instancio.of(modelGenerator.getNotificationModel())
                .create();

        testNotification.setTeam(Set.of(userRepository.findByEmail("maria@example.com").get()));
    }

    @AfterEach
    public void clean() {
        notificationRepository.deleteAll();
    }

    @Test
    public void testIndex() throws Exception {
        notificationRepository.save(testNotification);
        var result = mockMvc.perform(get("/api/notifications").with(jwt()))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        notificationRepository.save(testNotification);
        var notification = notificationRepository.findById(testNotification.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found!"));
        var request = get("/api/notifications/{id}", notification.getId()).with(jwt());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("text").isEqualTo(testNotification.getText())
        );
    }

    @Test
    public void testCreate() throws Exception {
        var notificationCreate = notificationMapper.map(testNotification);
        var request = post("/api/notifications").with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(notificationCreate));

        var mvcResult = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        var node = om.readTree(mvcResult);
        Long id = node.get("id").asLong();

        var notification = notificationRepository.findById(id).get();
        assertThat(notification.getType()).isEqualTo(testNotification.getType());
    }

    @Test
    public void testUpdate() throws Exception {
        notificationRepository.save(testNotification);
        var text = faker.hearthstone().mainCharacter();
        var data = new HashMap<>();
        data.put("text", text);

        var request = put("/api/notifications/{id}", testNotification.getId()).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        var result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("text").isEqualTo(text)
        );
    }

    @Test
    public void testDestroy() throws Exception {
        notificationRepository.save(testNotification);
        var id = testNotification.getId();
        var request = delete("/api/notifications/{id}", id).with(jwt());
        mockMvc.perform(request).andExpect(status().isNoContent());
        assertThat(notificationRepository.findById(id)).isEmpty();
    }
}