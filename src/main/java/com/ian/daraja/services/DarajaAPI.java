package com.ian.daraja.services;

import com.ian.daraja.dto.AccessTokenResponse;
import com.ian.daraja.dto.C2BTransactionRequest;
import com.ian.daraja.dto.C2BTransactionResponse;
import com.ian.daraja.dto.RegisterUrlResponse;

import java.io.IOException;

public interface DarajaAPI {
    AccessTokenResponse getAccessToken();

    RegisterUrlResponse registerUrl();
    C2BTransactionResponse simulateC2BTransaction(C2BTransactionRequest c2BTransactionRequest) throws IOException;
}
