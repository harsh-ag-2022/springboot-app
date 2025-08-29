package com.bajajfinserv.javaqualifier.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebhookGenerationRequest {
    private String name;
    private String regNo;
    private String email;
}