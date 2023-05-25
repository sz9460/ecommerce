package com.example.order.entity;

import com.example.basedomains.dto.Item;
import com.example.order.model.OrderItem;
import com.example.order.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Order {
    @Id
    private String id;
    private  Integer idUser;
    private Boolean isPayed;
    private Double montantTotal;
    private OrderStatus orderStatus;
    private Set<Item> items;

}
