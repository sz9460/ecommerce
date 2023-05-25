package com.example.payement.kafka;

import com.example.basedomains.dto.ProductPayementEvent;
import com.example.basedomains.dto.UserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PayementConsumer{

    private static final Logger LOGGER= LoggerFactory.getLogger(PayementConsumer.class);
    private String message;
    @KafkaListener(topics = "${spring.kafka.topic.name1}",groupId = "${spring.kafka.consumer.group-id}")
    public  void  consume(ProductPayementEvent event){
        LOGGER.info(String.format("Produc event recieved in payement service => %s",event.toString()));
        message = event.getMessage();

    }
    public String getMessage() {
        return message;
    }



}
