package com.example.bestellsystem.controller;

import com.example.bestellsystem.model.Order;
import com.example.bestellsystem.model.User;
import com.example.bestellsystem.repository.OrderRepository;
import com.example.bestellsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String listOrders(Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        model.addAttribute("orders", orderRepository.findByUser(user));
        return "orders";
    }

    @GetMapping("/new")
    public String showOrderForm(Model model) {
        model.addAttribute("order", new Order());
        return "order-form";
    }

    @PostMapping
    public String saveOrder(@ModelAttribute Order order, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        order.setUser(user);
        orderRepository.save(order);
        return "redirect:/orders";
    }
}
