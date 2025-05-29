package app.code.controller;

import app.code.mapper.ActivityMapper;
import app.code.model.Activity;
import app.code.repository.ActivityRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import app.code.exception.ResourceNotFoundException;
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

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ActivityControllerTest {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ModelGenerator modelGenerator;

    private Activity testActivity;


    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(springSecurity())
                .build();
        testActivity = Instancio.of(modelGenerator.getActivityModel())
                .create();
    }

    @AfterEach
    public void clean() {
        activityRepository.deleteAll();
    }

    @Test
    public void testIndex() throws Exception {
        activityRepository.save(testActivity);
        var result = mockMvc.perform(get("/api/activities").with(jwt()))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        activityRepository.save(testActivity);
        var activity = activityRepository.findById(testActivity.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found!"));
        var request = get("/api/activities/{id}", activity.getId()).with(jwt());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("type").isEqualTo(activity.getType())
        );
    }

    @Test
    public void testCreate() throws Exception {
        var createActivity = activityMapper.create(testActivity);
        var request = post("/api/activities").with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(createActivity));

        var mvcResult = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        var node = om.readTree(mvcResult);
        Long id = node.get("id").asLong();

        var activity = activityRepository.findById(id).get();
        assertThat(activity.getType()).isEqualTo(testActivity.getType());
    }

    @Test
    public void testUpdate() throws Exception {
        activityRepository.save(testActivity);
        var content = faker.hearthstone().mainCharacter();
        var data = new HashMap<>();
        data.put("content", content);

        var activity = activityRepository.findById(testActivity.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found!"));


        var request = put("/api/activities/{id}", activity.getId()).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        var mvcResult = mockMvc.perform(request).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        var node = om.readTree(mvcResult);
        Long id = node.get("id").asLong();

        activity = activityRepository.findById(id).get();
        assertThat(activity.getContent()).isEqualTo(content);
    }

    @Test
    public void testDestroy() throws Exception {
        activityRepository.save(testActivity);
        var activity = activityRepository.findById(testActivity.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found!"));

        var request = delete("/api/activities/{id}", activity.getId()).with(jwt());
        mockMvc.perform(request).andExpect(status().isNoContent());

        assertThat(activityRepository.findById(testActivity.getId())).isEmpty();
    }
}
