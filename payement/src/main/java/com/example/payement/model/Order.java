package com.example.payement.model;

import com.example.basedomains.dto.Item;
import lombok.Data;

import java.util.Set;
@Data
public class Order {
    private String id;
    private  Integer idUser;
    private Boolean isPayed;
    private Double montantTotal;
    private String orderStatus;
    private Set<Item> items;
}
