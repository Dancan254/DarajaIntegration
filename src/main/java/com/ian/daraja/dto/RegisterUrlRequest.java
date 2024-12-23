package com.ian.daraja.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class RegisterUrlRequest {
    @JsonProperty("ShortCode")
    private String shortCode;
    @JsonProperty("ResponseType")
    private String responseType;
    @JsonProperty("ConfirmationURL")
    private String confirmationUrl;
    @JsonProperty("ValidationURL")
    private String validationUrl;

}
