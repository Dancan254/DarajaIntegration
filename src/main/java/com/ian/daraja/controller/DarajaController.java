package com.ian.daraja.controller;

import com.ian.daraja.dto.AccessTokenResponse;
import com.ian.daraja.dto.RegisterUrlResponse;
import com.ian.daraja.services.DarajaAPI;
import com.ian.daraja.services.DarajaApiImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/daraja")
@RequiredArgsConstructor
public class DarajaController {
    private final DarajaAPI darajaAPI;

    @GetMapping(value = "/access-token", produces = "application/json")
    public ResponseEntity<AccessTokenResponse> getAccessToken() {
        try{
            return ResponseEntity.ok(darajaAPI.getAccessToken());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "/register-url", produces = "application/json")
    public ResponseEntity<RegisterUrlResponse> registerUrl() {
        try{
            return ResponseEntity.ok(darajaAPI.registerUrl());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> urRegister() throws IOException {
        return new ResponseEntity<>(darajaAPI.registerUrl2(), HttpStatus.OK);
    }
}
