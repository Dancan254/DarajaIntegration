package com.ian.daraja.controller;

import com.ian.daraja.dto.*;
import com.ian.daraja.services.DarajaAPI;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/v1/daraja")
public class DarajaController {
    private final DarajaAPI darajaAPI;
    private final AcknowledgementResponse acknowledgementResponse;

    public DarajaController(DarajaAPI darajaAPI, AcknowledgementResponse acknowledgementResponse){
        this.acknowledgementResponse = acknowledgementResponse;
        this.darajaAPI = darajaAPI;
    }
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
    public ResponseEntity<AcknowledgementResponse> validation(@Valid @RequestBody MpesaValidationResponse mpesaValidationResponse) {
        try{
            log.info("Received validation request: {}", mpesaValidationResponse);
            return ResponseEntity.ok(acknowledgementResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping(value = "/confirmation", produces = "application/json")
    public ResponseEntity<String> handleConfirmation(@Valid @RequestBody ConfirmationRequest confirmationRequest) {
        try {
            log.info("Received confirmation request: {}", confirmationRequest);
            return ResponseEntity.ok("Confirmation received successfully");
        } catch (Exception e) {
            log.error("Error processing confirmation request: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error processing confirmation request");
        }
    }

    @PostMapping(value = "c2b/simulate", produces = "application/json")
    public ResponseEntity<C2BTransactionResponse> simulateC2BTransaction(@Valid @RequestBody C2BTransactionRequest c2BTransactionRequest) {
        try{
            return ResponseEntity.ok(darajaAPI.simulateC2BTransaction(c2BTransactionRequest));
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping(value = "b2c/payment-request", produces = "application/json")
    public ResponseEntity<B2CResponse> b2cPaymentRequest(@RequestBody B2CRequest b2CRequest) {
        try{
            return ResponseEntity.ok(darajaAPI.b2cPaymentRequest(b2CRequest));
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping(value = "stk/push", produces = "application/json")
    public ResponseEntity<StkPushResponse> stkPush(@Valid @RequestBody StkPushRequest stkPushRequest) {
        try{
            return ResponseEntity.ok(darajaAPI.stkPush(stkPushRequest));
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "stkpush/callback", produces = "application/json")
    public ResponseEntity<AcknowledgementResponse> stkPushFallback(@Valid @RequestBody StkPushCallback stkPushCallback) {
        try {
            log.info("Received STK Push callback: {}", stkPushCallback);

            if (stkPushCallback.getResultCode() == 1032) {
                log.info("User canceled the STK Push request.");
            } else {
                log.info("STK Push request completed with result code: {}", stkPushCallback.getResultCode());
            }
            return ResponseEntity.ok(acknowledgementResponse);
        } catch (Exception e) {
            log.error("Error processing STK Push callback: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
