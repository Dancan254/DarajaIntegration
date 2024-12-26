package com.ian.daraja.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class B2CRequest {
    @JsonProperty("OriginatorConversationID")
    private String originatorConversationID;
    @JsonProperty("InitiatorName")
    private String initiatorName;

    @JsonProperty("SecurityCredential")
    private String securityCredential;

    @JsonProperty("CommandID")
    private String commandID;

    @JsonProperty("Amount")
    private int amount;

    @JsonProperty("PartyA")
    private int partyA;

    @JsonProperty("PartyB")
    private String partyB;

    @JsonProperty("Remarks")
    private String remarks;

    @JsonProperty("QueueTimeOutURL")
    private String queueTimeOutURL;

    @JsonProperty("ResultURL")
    private String resultURL;

    @JsonProperty("Occasion")
    private String occasion;
}