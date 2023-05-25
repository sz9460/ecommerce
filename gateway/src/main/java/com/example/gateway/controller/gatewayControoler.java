package com.example.gateway.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class gatewayControoler {
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<String> hiAdmin(){
    return Mono.just("hi i am admin");}

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public Mono<String> hiUser(){
        return Mono.just("hi i am user");}
}
