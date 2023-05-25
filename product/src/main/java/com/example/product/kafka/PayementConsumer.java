package com.example.product.kafka;

import com.example.basedomains.dto.*;
import com.example.product.entity.Product;
import com.example.product.repos.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PayementConsumer {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductProducer productProducer;
    private static final Logger LOGGER= LoggerFactory.getLogger(OrderConsumer.class);
    @KafkaListener(topics = "${spring.kafka.topic.name2}",groupId = "${spring.kafka.consumer.group-id}")
    public  void  consume(PayementProductEvent event){
        LOGGER.info(String.format("Payement event recieved in stock service => %s",event.toString()));
        ProductPayementEvent productPayementEvent =new ProductPayementEvent();
        for(ItemOrder item : event.getItems()){
            Product product = productRepository.findById(item.getIdProduct()).get();
            if (product.getQuantityStock() < item.getQuantity()){
                productPayementEvent.setMessage("False");
                break;
            }
            else {
                productPayementEvent.setMessage("True");

            }
        }

        productProducer.sendMessage(productPayementEvent);
    }
}
