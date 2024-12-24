package com.ian.daraja.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class C2BTransactionRequest {
    @JsonProperty("ShortCode")
    private String shortCode;
    @JsonProperty("CommandID")
    private String commandID;
    @JsonProperty("Amount")
    private String amount;
    @JsonProperty("Msisdn")
    private String mSisdn;
    @JsonProperty("BillRefNumber")
    private String billRefNumber;
}
