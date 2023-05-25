package com.example.product.kafka;
import com.example.basedomains.dto.UserEvent;
import com.example.product.entity.Favory;
import com.example.product.repos.FavoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserConsumer {
    @Autowired
    FavoryRepository favoryRepository;
    private static final Logger LOGGER= LoggerFactory.getLogger(UserConsumer.class);
    @KafkaListener(topics = "${spring.kafka.topic.name}",groupId = "${spring.kafka.consumer.group-id}")
    public  void  consume(UserEvent event){
        LOGGER.info(String.format("Order event recieved in stock service => %s",event.toString()));
        Favory favory =new Favory();
        favory.setIdUser(event.getIdUser());
        favoryRepository.save(favory);
    }
}
