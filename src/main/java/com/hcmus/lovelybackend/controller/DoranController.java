package com.hcmus.lovelybackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/")
public class DoranController {
    @GetMapping("/")
    public ResponseEntity<?> sayHello() {
        var response = Map.of("message", "Hi, I'm Doran Backend!");
        return ResponseEntity.ok(response);
    }
}
