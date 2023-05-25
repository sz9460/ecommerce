package com.example.authentication_server.kafka;
import com.example.basedomains.dto.UserEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class Producer {
    private static final Logger LOGGER= LoggerFactory.getLogger(Producer.class);
    private NewTopic topic;
    private KafkaTemplate<String, UserEvent> kafkaTemplate;

    public Producer(NewTopic topic, KafkaTemplate<String, UserEvent> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendMessage(UserEvent event){
        LOGGER.info(String.format("User event => %s",event.toString()));

        // create Message
        Message<UserEvent> message= MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC,topic.name())
                .build();
        kafkaTemplate.send(message);
    }
}
