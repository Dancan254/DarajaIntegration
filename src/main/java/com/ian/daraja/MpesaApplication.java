package com.ian.daraja;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ian.daraja.dto.AcknowledgementResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import okhttp3.*;

@SpringBootApplication
public class MpesaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MpesaApplication.class, args);
	}

	@Bean
	public OkHttpClient okHttpClient() {
		return new OkHttpClient();
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
	@Bean
	public AcknowledgementResponse getAcknowledgementResponse() {
		AcknowledgementResponse acknowledgementResponse = new AcknowledgementResponse();
		acknowledgementResponse.setResponseDescription("Accepted");
		return acknowledgementResponse;
	}
}
