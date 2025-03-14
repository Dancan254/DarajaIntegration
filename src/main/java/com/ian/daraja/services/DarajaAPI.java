package com.ian.daraja.services;

import com.ian.daraja.dto.*;

import java.io.IOException;

public interface DarajaAPI {
    AccessTokenResponse generateAccessToken();

    RegisterUrlResponse registerUrl();
    C2BTransactionResponse simulateC2BTransaction(C2BTransactionRequest c2BTransactionRequest) throws IOException;
    B2CResponse b2cPaymentRequest(B2CRequest b2CRequest) throws IOException;
    StkPushResponse stkPush(StkPushRequest stkPushRequest) throws IOException;
}
