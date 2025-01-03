package hexlet.code.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.exception.ResourceNotFoundException;
import hexlet.code.app.mapper.LabelMapper;
import hexlet.code.app.model.Label;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.util.ModelGenerator;
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
public class LabelControllerTest {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private LabelMapper labelMapper;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ModelGenerator modelGenerator;

    private Label testLabel;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(springSecurity())
                .build();
        testLabel = Instancio.of(modelGenerator.getLabelModel())
                .create();
    }

    @AfterEach
    public void clean() {
        labelRepository.deleteAll();
    }

    @Test
    public void testIndex() throws Exception {
        labelRepository.save(testLabel);
        var result = mockMvc.perform(get("/api/labels").with(jwt()))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        labelRepository.save(testLabel);
        var label = labelRepository.findByName(testLabel.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Label not found!"));
        var request = get("/api/labels/{id}", label.getId()).with(jwt());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(label.getName())
        );
    }

    @Test
    public void testCreate() throws Exception {
        var createLabel = labelMapper.create(testLabel);
        var request = post("/api/labels").with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(createLabel));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var label = labelRepository.findByName(testLabel.getName()).get();
        assertThat(label.getName()).isEqualTo(testLabel.getName());
    }

    @Test
    public void testUpdate() throws Exception {
        labelRepository.save(testLabel);
        var name = faker.hearthstone().mainCharacter();
        var data = new HashMap<>();
        data.put("name", name);

        var label = labelRepository.findByName(testLabel.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Label not found!"));

        var request = put("/api/labels/{id}", label.getId()).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request).andExpect(status().isOk());

        label = labelRepository.findByName(name).get();
        assertThat(label.getName()).isEqualTo(name);
    }

    @Test
    public void testDestroy() throws Exception {
        labelRepository.save(testLabel);
        var label = labelRepository.findByName(testLabel.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Label not found!"));

        var request = delete("/api/labels/{id}", label.getId()).with(jwt());
        mockMvc.perform(request).andExpect(status().isNoContent());

        assertThat(labelRepository.findByName(testLabel.getName())).isEmpty();
    }
}
