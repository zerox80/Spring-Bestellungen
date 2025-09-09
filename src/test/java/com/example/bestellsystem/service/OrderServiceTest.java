package com.example.bestellsystem.service;

import com.example.bestellsystem.model.Order;
import com.example.bestellsystem.model.User;
import com.example.bestellsystem.repository.OrderRepository;
import com.example.bestellsystem.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderService orderService;

    @Mock
    private Principal principal;

    @Test
    void findOrdersForCurrentUser_shouldCallRepositoryWithCorrectUser() {
        // Arrange
        String username = "testuser";
        User user = new User();
        user.setUsername(username);

        when(principal.getName()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        orderService.findOrdersForCurrentUser(principal);

        // Assert
        verify(orderRepository).findByUser(user);
    }

    @Test
    void saveOrderForCurrentUser_shouldSetUserAndSaveOrder() {
        // Arrange
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        Order order = new Order();

        when(principal.getName()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        orderService.saveOrderForCurrentUser(order, principal);

        // Assert
        verify(userRepository).findByUsername(username);
        verify(orderRepository).save(order);
        assertEquals(user, order.getUser());
    }
}
