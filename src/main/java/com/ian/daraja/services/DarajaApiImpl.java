package com.ian.daraja.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.ian.daraja.config.MpesaConfig;
import com.ian.daraja.dto.AccessTokenResponse;
import com.ian.daraja.dto.RegisterUrlRequest;
import com.ian.daraja.dto.RegisterUrlResponse;
import com.ian.daraja.utils.HelperUtility;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@Service
public class DarajaApiImpl implements DarajaAPI {
    private final MpesaConfig mpesaConfig;
    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;
    public DarajaApiImpl(MpesaConfig mpesaConfig, OkHttpClient okHttpClient, ObjectMapper objectMapper) {
        this.okHttpClient = okHttpClient;
        this.mpesaConfig = mpesaConfig;
        this.objectMapper = objectMapper;
    }
    @Override
    public AccessTokenResponse getAccessToken() {
        String encodedCredentials = HelperUtility.toBase64(mpesaConfig.getConsumerKey() + ":" + mpesaConfig.getConsumerSecret());
        String url = String.format("%s?grant_type=%s", mpesaConfig.getOauthEndpoint(), mpesaConfig.getGrantType());
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", "Basic " + encodedCredentials)
                .addHeader("Content-Type", "application/json")
                .build();
        log.info("Sending request to URL: {}", url);
        log.info("Authorization header: Basic {}", encodedCredentials);
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                log.error("Request failed with status code: {}", response.code());
                return null;
            }
            String responseBody = response.body() != null ? response.body().string() : "";
            if (responseBody.isEmpty()) {
                log.error("Received empty response body");
                return null;
            }

            log.info("Received response: {}", responseBody);
            return objectMapper.readValue(responseBody, AccessTokenResponse.class);
        } catch (IOException e) {
            log.error("Error occurred while fetching access token: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public RegisterUrlResponse registerUrl() {
        // Get access token
        AccessTokenResponse accessToken = getAccessToken();
        if (accessToken == null) {
            log.error("Failed to retrieve access token");
            return null;
        }
        // Build the request DTO
        RegisterUrlRequest registerUrlRequest = new RegisterUrlRequest();
        registerUrlRequest.setShortCode(mpesaConfig.getShortCode());
        registerUrlRequest.setResponseType(mpesaConfig.getResponseType());
        registerUrlRequest.setConfirmationUrl(mpesaConfig.getConfirmationUrl());
        registerUrlRequest.setValidationUrl(mpesaConfig.getValidationUrl());
        // Serialize the request body to JSON
        String requestBodyJson = HelperUtility.toJson(registerUrlRequest);
        if (requestBodyJson == null) {
            log.error("Failed to serialize request body to JSON");
            return null;
        }
        // Prepare the HTTP request
        RequestBody requestBody = RequestBody.create(requestBodyJson, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(mpesaConfig.getUrlRegistrationEndpoint())
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + accessToken.getAccessToken())
                .addHeader("Content-Type", "application/json")
                .build();

        log.info("Sending register URL request to: {}", mpesaConfig.getUrlRegistrationEndpoint());
        log.info("Request body: {}", requestBodyJson);

        // Execute the request and handle the response
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "No error details provided";
                log.error("Request failed with status code: {}", response.code());
                log.error("Response body: {}", errorBody);
                return null;
            }
            String responseBody = response.body() != null ? response.body().string() : "";
            if (responseBody.isEmpty()) {
                log.error("Received empty response body");
                return null;
            }
            log.info("Received response: {}", responseBody);
            return objectMapper.readValue(responseBody, RegisterUrlResponse.class);
        } catch (IOException e) {
            log.error("Error occurred while registering confirmation and validation URLs: {}", e.getMessage());
            return null;
        }
    }
    /*
    * This method is used to register the URLs for the C2B API
    * I used this when the initial impl had hiccups
    * I picked it from the code generated by daraja api docs
    *@return String
     */

    /*
    public String registerUrl2() throws IOException {
        // Prepare the request body
        RequestBody requestBody = RequestBody.create(
                "{\"ShortCode\":77,\"ResponseType\":\"Completed\",\"ConfirmationURL\":\"https://mydomain.com/confirmation\",\"ValidationURL\":\"https://mydomain.com/validation\"}",
                MediaType.get("application/json")
        );
        log.info("Request body: {}", requestBody);
        // Create the request
        Request request = new Request.Builder()
                .url("https://sandbox.safaricom.co.ke/mpesa/c2b/v1/registerurl")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer ")
                .build();
        log.info("Request: {}", request);
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response.code());
            }
            log.info("Response: {}", response);
            return response.body().string();
        }
    }
     */
}