package com.example.bestellsystem.controller;

import com.example.bestellsystem.model.Order;
import com.example.bestellsystem.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String listOrders(Model model, Principal principal) {
        model.addAttribute("orders", orderService.findOrdersForCurrentUser(principal));
        return "orders";
    }

    @GetMapping("/new")
    public String showOrderForm(Model model) {
        model.addAttribute("order", new Order());
        return "order-form";
    }

    @PostMapping
    public String saveOrder(@Valid @ModelAttribute("order") Order order, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "order-form";
        }
        orderService.saveOrderForCurrentUser(order, principal);
        return "redirect:/orders";
    }
}
