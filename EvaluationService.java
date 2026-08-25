package com.rikkeipay.autoeval.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rikkeipay.autoeval.client.LLMClient;
import com.rikkeipay.autoeval.client.LangfuseClient;
import com.rikkeipay.autoeval.model.EvaluationResult;
import com.rikkeipay.autoeval.model.WebhookEvent;
import org.springframework.stereotype.Service;

@Service
public class EvaluationService {

    private final LLMClient llmClient;
    private final LangfuseClient langfuseClient;
    private final ObjectMapper objectMapper;

    public EvaluationService(LLMClient llmClient, LangfuseClient langfuseClient, ObjectMapper objectMapper) {
        this.llmClient = llmClient;
        this.langfuseClient = langfuseClient;
        this.objectMapper = objectMapper;
    }

    public void processWebhookEvent(WebhookEvent event) {
        if (!"trace.create".equals(event.getEventName()) && !"trace.update".equals(event.getEventName())) {
            return;
        }

        WebhookEvent.WebhookData data = event.getData();
        if (data == null || data.getId() == null) {
            return;
        }

        String inputStr = extractString(data.getInput());
        String outputStr = extractString(data.getOutput());

        if (inputStr.isEmpty() || outputStr.isEmpty()) {
            return;
        }

        EvaluationResult result = llmClient.evaluate(inputStr, outputStr);

        langfuseClient.postScore(data.getId(), "accuracy", result.getAccuracy().getScore(), result.getAccuracy().getReason());
        langfuseClient.postScore(data.getId(), "politeness", result.getPoliteness().getScore(), result.getPoliteness().getReason());
        langfuseClient.postScore(data.getId(), "security", result.getSecurity().getScore(), result.getSecurity().getReason());
    }

    private String extractString(Object obj) {
        if (obj == null) return "";
        if (obj instanceof String) return (String) obj;
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return obj.toString();
        }
    }
}