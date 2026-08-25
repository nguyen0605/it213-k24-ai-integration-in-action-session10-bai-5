package com.rikkeipay.autoeval.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Component
public class LangfuseClient {

    private final WebClient webClient;

    public LangfuseClient(WebClient.Builder webClientBuilder,
                           @Value("${langfuse.baseUrl}") String baseUrl,
                           @Value("${langfuse.publicKey}") String publicKey,
                           @Value("${langfuse.privateKey}") String privateKey) {
        String authHeader = "Basic " + Base64.getEncoder().encodeToString(
                (publicKey + ":" + privateKey).getBytes(StandardCharsets.UTF_8)
        );

        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", authHeader)
                .build();
    }

    public void postScore(String traceId, String name, double value, String comment) {
        Map<String, Object> requestBody = Map.of(
                "traceId", traceId,
                "name", name,
                "value", value,
                "comment", comment
        );

        try {
            webClient.post()
                    .uri("/api/v1/scores")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (Exception e) {
            System.err.println("Failed to push score to Langfuse: " + e.getMessage());
        }
    }
}