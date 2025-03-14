package com.example.service_discovery;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@Slf4j
public class ServiceConsumerController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/consume-service")
    public String consumeService() {
        String serviceUrl = "http://EUREKA-CLIENT:8080/hostname";
        return restTemplate.getForObject(serviceUrl, String.class);
    }
}