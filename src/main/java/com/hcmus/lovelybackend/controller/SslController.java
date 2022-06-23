package com.hcmus.lovelybackend.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Controller
@RequestMapping(".well-known/pki-validation/319E22B8FBE1EEC5384E8A2A7F277A3C.txt")
public class SslController {
    @GetMapping
    public ResponseEntity<?> getFile() throws IOException {
        var file = ResourceUtils.getFile("classpath:keystore/319E22B8FBE1EEC5384E8A2A7F277A3C.txt");

        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(Files.readAllBytes(Path.of(file.getPath()))));
    }
}
