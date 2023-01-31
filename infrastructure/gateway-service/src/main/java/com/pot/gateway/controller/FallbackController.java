package com.pot.gateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @RequestMapping(value = "/authFallBack")
    public Mono<String> authServiceFallBack(){
        return Mono.just("Auth Service is taking too long to respond or is down. Please try again later");
    }

    @RequestMapping("/userFallBack")
    public Mono<String> userServiceFallBack(){
        return Mono.just("User Service is taking too long to respond or is down. Please try again later");
    }

    @RequestMapping("/benefitFallBack")
    public Mono<String> benefitServiceFallBack(){
        return Mono.just("Benefit Service is taking too long to respond or is down. Please try again later");
    }
}
