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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;

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
    public String listOrders(Model model, Principal principal,
                             @RequestParam(name = "page", defaultValue = "0") int page,
                             @RequestParam(name = "size", defaultValue = "10") int size) {
        Page<Order> ordersPage = orderService.findOrdersForCurrentUserPaged(principal, page, size);
        model.addAttribute("ordersPage", ordersPage);
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
