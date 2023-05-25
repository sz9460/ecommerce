package com.example.order.repos;

import com.example.order.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepos   extends MongoRepository<Order,String> {

    List<Order> findOrdersByIdUser(Integer idUser);
}
