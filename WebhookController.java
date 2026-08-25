package com.rikkeipay.autoeval.controller;

import com.rikkeipay.autoeval.model.WebhookEvent;
import com.rikkeipay.autoeval.service.EvaluationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/webhooks")
public class WebhookController {

    private final EvaluationService evaluationService;

    public WebhookController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @PostMapping("/langfuse")
    public ResponseEntity<Void> handleLangfuseWebhook(@RequestBody WebhookEvent event) {
        try {
            evaluationService.processWebhookEvent(event);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}