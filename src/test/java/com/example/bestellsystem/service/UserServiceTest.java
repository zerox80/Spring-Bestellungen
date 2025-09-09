package com.example.bestellsystem.service;

import com.example.bestellsystem.model.User;
import com.example.bestellsystem.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_shouldSaveUser_whenUsernameIsNew() throws UsernameAlreadyExistsException {
        // Arrange
        User user = new User();
        user.setUsername("newUser");
        user.setPassword("password");

        when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        // Act
        userService.registerUser(user);

        // Assert
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_shouldThrowException_whenUsernameExists() {
        // Arrange
        User user = new User();
        user.setUsername("existingUser");
        user.setPassword("password");

        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(new User()));

        // Act & Assert
        assertThrows(UsernameAlreadyExistsException.class, () -> {
            userService.registerUser(user);
        });

        verify(userRepository, never()).save(any(User.class));
    }
}
