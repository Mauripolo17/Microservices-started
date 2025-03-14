package com.example.eureka_client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HostNameController {
    private final Environment environment;

    @Autowired
    public HostNameController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping(path = "/hostname")
    public ResponseEntity<String> hostname() {
        String hostname = environment.getProperty("HOSTNAME");
        return ResponseEntity.ok().body(hostname);
    }
}
