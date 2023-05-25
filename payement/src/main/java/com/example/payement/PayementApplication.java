package com.example.payement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PayementApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayementApplication.class, args);
    }

}
