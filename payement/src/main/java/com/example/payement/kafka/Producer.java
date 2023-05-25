package com.example.payement.kafka;
import com.example.basedomains.dto.PayemantEvent;
import com.example.basedomains.dto.PayementProductEvent;
import com.example.basedomains.dto.UserEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
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
public class Producer {
    private static final Logger LOGGER= LoggerFactory.getLogger(Producer.class);
    private NewTopic topic;


    private KafkaTemplate<String, PayementProductEvent> kafkaTemplate1;
    public Producer(NewTopic topic ,KafkaTemplate<String, PayementProductEvent> kafkaTemplate1) {
        this.topic = topic;
        this.kafkaTemplate1 = kafkaTemplate1;
    }



    public void sendMessage1(PayementProductEvent event){
        LOGGER.info(String.format("Payement Product event => %s",event.toString()));

        // create Message
        Message<PayementProductEvent> message= MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC,topic.name())
                .build();
        kafkaTemplate1.send(message);
    }
}
