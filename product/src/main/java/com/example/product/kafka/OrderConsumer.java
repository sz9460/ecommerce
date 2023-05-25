package com.example.product.kafka;

import com.example.basedomains.dto.Item;
import com.example.basedomains.dto.OrderEvent;
import com.example.product.entity.Product;
import com.example.product.repos.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {
    @Autowired
    ProductRepository productRepository;
    private static final Logger LOGGER= LoggerFactory.getLogger(OrderConsumer.class);
    @KafkaListener(topics = "${spring.kafka.topic.name1}",groupId = "${spring.kafka.consumer.group-id}")
    public  void  consume(OrderEvent event){
        LOGGER.info(String.format("Order event recieved in stock service => %s",event.toString()));
        for(Item item : event.getItems()){
            Product product = productRepository.findById(item.getIdProduct()).get();
            product.setQuantityStock(product.getQuantityStock()-item.getQuantity());
            productRepository.save(product);
        }

    }
}
