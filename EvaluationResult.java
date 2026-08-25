package com.rikkeipay.autoeval.model;

import lombok.Data;

@Data
public class EvaluationResult {
    private Dimension accuracy;
    private Dimension politeness;
    private Dimension security;

    @Data
    public static class Dimension {
        private int score;
        private String reason;
    }
}