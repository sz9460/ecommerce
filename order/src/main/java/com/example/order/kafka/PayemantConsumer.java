package com.example.order.kafka;

import com.example.basedomains.dto.Item;
import com.example.basedomains.dto.OrderEvent;
import com.example.basedomains.dto.PayemantEvent;
import com.example.order.entity.Order;
import com.example.order.model.OrderStatus;
import com.example.order.repos.OrderRepos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PayemantConsumer {
    @Autowired
    OrderRepos orderRepos;

    @Autowired
    OrderProducer orderProducer;
    private static final Logger LOGGER= LoggerFactory.getLogger(PayemantConsumer.class);
    @KafkaListener(topics = "${spring.kafka.topic.name}",groupId = "${spring.kafka.consumer.group-id}")
    public  void  consume(PayemantEvent event){
        LOGGER.info(String.format("Order event recieved in stock service => %s",event.toString()));
        Order order = orderRepos.findById(event.getIdOrder()).get();
        if (event.getStatus().equals("approved")){
            order.setOrderStatus(OrderStatus.PROCESSING);
            order.setIsPayed(true);
            orderRepos.save(order);
            Set<Item> items = order.getItems();
            OrderEvent orderEvent =new OrderEvent();
            orderEvent.setItems(items);
            orderEvent.setMessage("order updated");
            orderProducer.sendMessage(orderEvent);

        }
        else {
            order.setOrderStatus(OrderStatus.DRAFT);
        }

    }
}
