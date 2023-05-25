package com.example.order.controller;

import com.example.basedomains.dto.Item;
import com.example.order.entity.Order;
import com.example.order.model.OrderItem;
import com.example.order.model.OrderStatus;
import com.example.order.repos.OrderRepos;
import com.example.order.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class OrderController {
    @Autowired
    OrderRepos orderRepos;

    @Autowired
    JwtService jwtService;

    @PostMapping("/create")
    public Order createOrder(@RequestBody Order order){
        Order order1 =new Order();
        order1.setIdUser(order.getIdUser());
        order1.setIsPayed(order.getIsPayed());
        order1.setMontantTotal(order.getMontantTotal());
        order1.setOrderStatus(OrderStatus.DRAFT);
        order1.setItems(order.getItems());
        return orderRepos.save(order1);
    }

    @GetMapping("/{IdOrder}/Items")

    public Set<Item> getMesOrders(@PathVariable("IdOrder") String IdOrder){
        Order order = orderRepos.findById(IdOrder).get();
        return order.getItems();
    }

    @DeleteMapping("/delete/{oderId}")
    public void deleteOrder(@PathVariable("orderId") String orderId){
        orderRepos.deleteById(orderId);
    }





}
