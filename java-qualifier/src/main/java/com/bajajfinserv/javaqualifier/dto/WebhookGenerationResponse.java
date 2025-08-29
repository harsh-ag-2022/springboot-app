package com.bajajfinserv.javaqualifier.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WebhookGenerationResponse {
    // Assuming the JSON response keys are webhookUrl and accessToken
    @JsonProperty("webhookUrl")
    private String webhookUrl;

    @JsonProperty("accessToken")
    private String accessToken;
}