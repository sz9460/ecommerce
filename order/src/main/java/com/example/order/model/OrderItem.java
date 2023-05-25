package com.example.order.model;

import lombok.Data;

@Data
public class OrderItem {
    private String idProduct;
    private double price;
    private Integer quantity;
}
