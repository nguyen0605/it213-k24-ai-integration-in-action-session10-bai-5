package com.rikkeipay.autoeval.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookEvent {
    private String eventName;
    private WebhookData data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WebhookData {
        private String id;
        private String name;
        private Object input;
        private Object output;
        private String timestamp;
    }
}