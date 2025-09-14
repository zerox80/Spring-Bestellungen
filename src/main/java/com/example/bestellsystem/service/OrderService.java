package com.example.bestellsystem.service;

import com.example.bestellsystem.model.Order;
import com.example.bestellsystem.model.User;
import com.example.bestellsystem.repository.OrderRepository;
import com.example.bestellsystem.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.security.Principal;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<Order> findOrdersForCurrentUser(Principal principal) {
        User user = getUser(principal);
        return orderRepository.findByUser(user);
    }

    @Transactional(readOnly = true)
    public Page<Order> findOrdersForCurrentUserPaged(Principal principal, int page, int size) {
        User user = getUser(principal);
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1), Sort.by(Sort.Direction.DESC, "id"));
        return orderRepository.findByUser(user, pageable);
    }

    @Transactional
    public void saveOrderForCurrentUser(Order order, Principal principal) {
        User user = getUser(principal);
        order.setUser(user);
        orderRepository.save(order);
    }

    private User getUser(Principal principal) {
        if (principal == null) {
            throw new IllegalStateException("No authenticated principal found");
        }
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found in security principal"));
    }
}

