package com.ian.daraja.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class C2BTransactionResponse {
    @JsonProperty("ResponseCode")
    private String responseCode;

    @JsonProperty("ResponseDescription")
    private String responseDescription;

    @JsonProperty("OriginatorCoversationID")
    private String originatorCoversationID;
}
