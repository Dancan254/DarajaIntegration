package com.ian.daraja.services;

import com.ian.daraja.dto.AccessTokenResponse;
import com.ian.daraja.dto.RegisterUrlResponse;

import java.io.IOException;

public interface DarajaAPI {
    AccessTokenResponse getAccessToken();

    RegisterUrlResponse registerUrl();
}
