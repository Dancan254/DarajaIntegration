package com.ian.daraja.controller;

import com.ian.daraja.dto.AccessTokenResponse;
import com.ian.daraja.dto.AcknowledgementResponse;
import com.ian.daraja.dto.MpesaValidationResponse;
import com.ian.daraja.dto.RegisterUrlResponse;
import com.ian.daraja.services.DarajaAPI;
import com.ian.daraja.services.DarajaApiImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/v1/daraja")
@RequiredArgsConstructor
public class DarajaController {
    private final DarajaAPI darajaAPI;
    private final AcknowledgementResponse acknowledgementResponse;
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

    @PostMapping(value = "/validation", produces = "application/json")
    public ResponseEntity<AcknowledgementResponse> validation(@RequestBody MpesaValidationResponse mpesaValidationResponse) {
        try{
            log.info("Received validation request: {}", mpesaValidationResponse);
            return ResponseEntity.ok(acknowledgementResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
