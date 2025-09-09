package com.example.bestellsystem;

import com.example.bestellsystem.model.User;
import com.example.bestellsystem.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class BestellsystemApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // Clean up any existing test data
        userRepository.deleteAll();
    }

    @Test
    void contextLoads() {
    }

    @Test
    void testPublicHomePage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void testAccessSecuredPageWithoutAuthRedirectsToLogin() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void testRegistration() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "testuser")
                        .param("password", "password")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        Optional<User> user = userRepository.findByUsername("testuser");
        assertTrue(user.isPresent());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testAccessSecuredPageWithAuth() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }

    @Test
    void testRegistrationWithInvalidData() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "") // Blank username
                        .param("password", "short") // Password too short
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().hasErrors());

        assertTrue(userRepository.findAll().isEmpty());
    }

    @Test
    void testRegistrationWithDuplicateUsername() throws Exception {
        // Create an initial user
        User existingUser = new User();
        existingUser.setUsername("existinguser");
        existingUser.setPassword("password123");
        userRepository.save(existingUser);

        mockMvc.perform(post("/register")
                        .param("username", "existinguser")
                        .param("password", "newpassword123")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrorCode("user", "username", "user.exists"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testAuthenticatedUserCanSeeOrders() throws Exception {
        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        userRepository.save(testUser);

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testAuthenticatedUserCanCreateOrder() throws Exception {
        // Ensure the mock user exists in the repository for the service layer
        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        userRepository.save(testUser);

        mockMvc.perform(post("/orders")
                        .param("description", "A new laptop")
                        .param("shippingAddress", "123 Main St")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testCreateOrderWithInvalidData() throws Exception {
        mockMvc.perform(post("/orders")
                        .param("description", "") // Blank description
                        .param("shippingAddress", "")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("order-form"))
                .andExpect(model().hasErrors());
    }
}
