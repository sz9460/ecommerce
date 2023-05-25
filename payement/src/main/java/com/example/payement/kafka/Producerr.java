package com.example.payement.kafka;

import com.example.basedomains.dto.PayemantEvent;
import com.example.basedomains.dto.PayementProductEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class Producerr {
    private static final Logger LOGGER= LoggerFactory.getLogger(Producerr.class);
    @Autowired
    private KafkaTemplate<String, PayemantEvent> kafkaTemplate;
    @Value("${spring.kafka.topic.name2}")
    private String topicName;


    public void sendMessage(PayemantEvent event){
        LOGGER.info(String.format("Payement Order event => %s",event.toString()));

        // create Message
        Message<PayemantEvent> message= MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC,topicName)
                .build();
        kafkaTemplate.send(message);
    }

}
