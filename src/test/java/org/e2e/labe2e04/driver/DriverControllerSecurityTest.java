package org.e2e.labe2e04.driver;

import org.e2e.labe2e04.driver.infrastructure.DriverRepository;
import org.e2e.labe2e04.utils.Reader;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithUserDetails(value = "johndoe@example.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
@SpringBootTest
@AutoConfigureMockMvc
public class DriverControllerSecurityTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DriverRepository driverRepository;

    String token = "";

    @BeforeEach
    public void setUp() throws Exception {
        driverRepository.deleteAll();
        String jsonContent = Reader.readJsonFile("/driver/post.json");
        var res = mockMvc.perform(post("/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(jsonContent))
                .andReturn();
        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(res.getResponse().getContentAsString()));
        token = jsonObject.getString("token");
    }

    @Test
    public void testAuthorizedAccessToGetDriverById() throws Exception {
        Long authorizedDriverId = driverRepository
                .findByEmail("johndoe@example.com")
                .orElseThrow()
                .getId();
        mockMvc.perform(get("/driver/{id}", authorizedDriverId)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "PASSENGER")
    public void testPassengerAccessToGetDriverById() throws Exception {
        Long driverId = driverRepository
                .findByEmail("johndoe@example.com")
                .orElseThrow()
                .getId();
        mockMvc.perform(get("/driver/{id}", driverId)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testAuthorizedAccessToGetOwnDriverInfo() throws Exception {
        mockMvc.perform(get("/driver/me")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void testUnauthorizedAccessToGetOwnDriverInfo() throws Exception {
        mockMvc.perform(get("/driver/me")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    public void testUnauthorizedAccessToDeleteDriver() throws Exception {
        mockMvc.perform(delete("/driver/me")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testAuthorizedAccessToUpdateDriverInfo() throws Exception {
        String jsonContent = Reader.readJsonFile("/driver/patch.json");
        mockMvc.perform(patch("/driver/me")
                        .contentType(APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void testUnauthorizedAccessToUpdateDriverInfo() throws Exception {
        String jsonContent = Reader.readJsonFile("/driver/patch.json");
        mockMvc.perform(patch("/driver/me")
                        .contentType(APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testAuthorizedAccessToUpdateDriverCar() throws Exception {
        String jsonContent = Reader.readJsonFile("/vehicle/post.json");
        mockMvc.perform(patch("/driver/me/car")
                        .contentType(APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void testUnauthorizedAccessToUpdateDriverCar() throws Exception {
        String jsonContent = Reader.readJsonFile("/vehicle/post.json");
        mockMvc.perform(patch("/driver/me/car")
                        .contentType(APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isForbidden());
    }
}
