package com.ian.daraja.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StkPushCallback {
    @JsonProperty("MerchantRequestID")
    private String merchantRequestID;

    @JsonProperty("CheckoutRequestID")
    private String checkoutRequestID;

    @JsonProperty("ResultCode")
    private int resultCode;

    @JsonProperty("ResultDesc")
    private String resultDesc;
}