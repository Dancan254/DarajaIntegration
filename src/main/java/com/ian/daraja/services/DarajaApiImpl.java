package com.ian.daraja.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ian.daraja.config.MpesaConfig;
import com.ian.daraja.dto.*;
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
    public AccessTokenResponse generateAccessToken() {
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
        try(Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("Request failed with status code: {}", response.code());
                throw new RuntimeException("Failed to generate access token");
            }
            String responseBody = response.body() != null ? response.body().string() : "";
            if (responseBody.isEmpty()) {
                log.error("Received empty response body");
                throw new RuntimeException("Received empty response body");
            }
            log.info("Received response: {}", responseBody);
            return objectMapper.readValue(responseBody, AccessTokenResponse.class);
        } catch (IOException e) {
            log.error("Error occurred while fetching access token: {}", e.getMessage());
            throw new RuntimeException("Error occurred while fetching access token");
        }
    }

    @Override
    public RegisterUrlResponse registerUrl() {
        // Get access token
        AccessTokenResponse accessToken = generateAccessToken();
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
            throw new RuntimeException("Failed to serialize request body to JSON");
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
                log.error("Response body: {}", errorBody);
                throw new RuntimeException("Request failed with status code: " + response.code());
            }
            String responseBody = response.body() != null ? response.body().string() : "";
            if (responseBody.isEmpty()) {
                log.error("Received empty response body");
                throw new RuntimeException("Received empty response body");
            }
            log.info("Received response: {}", responseBody);
            return objectMapper.readValue(responseBody, RegisterUrlResponse.class);
        } catch (IOException e) {
            log.error("Error occurred while registering confirmation and validation URLs: {}", e.getMessage());
            throw new RuntimeException("Error occurred while registering confirmation and validation URLs");
        }
    }

    /**
     * @param transactionRequest
     * @return  C2BTransactionResponse
     * @throws IOException
     */
    @Override
    public C2BTransactionResponse simulateC2BTransaction(C2BTransactionRequest transactionRequest) throws IOException {
        //get the access token
        AccessTokenResponse accessToken = generateAccessToken();
        if (accessToken == null) {
            log.error("Failed to retrieve access token");
            return null;
        }
        // Prepare the request body
       RequestBody requestBody = RequestBody.create(Objects.requireNonNull(HelperUtility.toJson(transactionRequest)), MediaType.get("application/json; charset=utf-8"));
        // Prepare the request
        Request request = new Request.Builder()
                .url(mpesaConfig.getC2bTransactionEndpoint())
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + accessToken.getAccessToken())
                .addHeader("Content-Type", "application/json")
                .build();
        log.info("Sending C2B transaction request to: {}", mpesaConfig.getC2bTransactionEndpoint());
        log.info("Request body: {}", requestBody);
        // Execute the request and handle the response
        try(Response response = okHttpClient.newCall(request).execute()){
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "No error details provided";
                //log.error("Request failed with status code: {}", response.code());
                log.error("Response body: {}", errorBody);
                return null;
            }
            String responseBody = response.body().string();
            return objectMapper.readValue(responseBody, C2BTransactionResponse.class);
        } catch (IOException e) {
            log.error("Error occurred while simulating C2B transaction: {}", e.getMessage());
            return null;
        }
    }

    /**
     * @param b2CRequest
     * @return
     * @throws IOException
     */
    @Override
    public B2CResponse b2cPaymentRequest(B2CRequest b2CRequest) throws IOException {
        // Get the access token
        AccessTokenResponse accessToken = generateAccessToken();
        if (accessToken == null) {
            log.error("Failed to retrieve access token");
            return null;
        }
        // Prepare the request body
        RequestBody requestBody = RequestBody.create(Objects.requireNonNull(HelperUtility.toJson(b2CRequest)), MediaType.get("application/json; charset=utf-8"));
        // Prepare the request
        Request request = new Request.Builder()
                .url(mpesaConfig.getB2cTransactionEndpoint())
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + accessToken.getAccessToken())
                .addHeader("Content-Type", "application/json")
                .build();
        log.info("Sending B2C payment request to: {}", mpesaConfig.getB2cTransactionEndpoint());
        log.info("Request body: {}", requestBody);
        // Execute the request and handle the response
        try(Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "No error details provided";
                log.error("Request failed with status code: {}", response.code());
                log.error("Response body: {}", errorBody);
                return null;
            }
            String responseBody = response.body().string();
            return objectMapper.readValue(responseBody, B2CResponse.class);
        } catch (IOException e) {
            log.error("Error occurred while making B2C payment request: {}", e.getMessage());
            return null;
        }
    }

    /**
     * @param stkPushRequest
     * @return
     * @throws IOException
     */
    @Override
    public StkPushResponse stkPush(StkPushRequest stkPushRequest) throws IOException {
        AccessTokenResponse accessToken = generateAccessToken();
        if (accessToken == null) {
            log.error("Failed to retrieve access token");
        }
        RequestBody requestBody = RequestBody.create(Objects.requireNonNull(HelperUtility.toJson(stkPushRequest)),
                MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(mpesaConfig.getStkPushEndpoint())
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + accessToken.getAccessToken())
                .addHeader("Content-Type", "application/json")
                .build();
        log.info("Sending STK push request to: {}", mpesaConfig.getStkPushEndpoint());
        log.info("Request body: {}", requestBody);
       
        //execute the request and handle the response
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "No error details provided";
                log.error("Request failed with status code: {}", response.code());
                log.error("Response body: {}", errorBody);
                throw new RuntimeException("Error occurred while making STK push request");
            }
            String responseBody = response.body().string();
            return objectMapper.readValue(responseBody, StkPushResponse.class);
        } catch (IOException e) {
            log.error("Error occurred while making STK push request: {}", e.getMessage());
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