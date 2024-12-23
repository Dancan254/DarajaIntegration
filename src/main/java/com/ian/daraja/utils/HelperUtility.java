package com.ian.daraja.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
@Slf4j
public class HelperUtility {
    public static String toBase64(String value){
        byte[] bytes = value.getBytes(ISO_8859_1);
        return Base64.getEncoder().encodeToString(bytes);
    }
    public static String toJson(Object object){
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Error occurred: {}", e.getMessage());
        }
        return null;
    }
}
