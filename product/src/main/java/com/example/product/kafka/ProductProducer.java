package com.example.product.kafka;

import com.example.basedomains.dto.PayemantEvent;
import com.example.basedomains.dto.ProductPayementEvent;
import com.example.basedomains.dto.UserEvent;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class ProductProducer {
    private static final Logger LOGGER= LoggerFactory.getLogger(ProductProducer.class);
    private NewTopic topic;
    private KafkaTemplate<String, PayemantEvent> kafkaTemplate;
    public ProductProducer(NewTopic topic, KafkaTemplate<String, PayemantEvent> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendMessage(ProductPayementEvent event){
        LOGGER.info(String.format("Product payement event => %s",event.toString()));

        // create Message
        Message<ProductPayementEvent> message= MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC,topic.name())
                .build();
        kafkaTemplate.send(message);
    }
}
