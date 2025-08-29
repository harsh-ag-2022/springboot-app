package com.bajajfinserv.javaqualifier.runner;

import com.bajajfinserv.javaqualifier.dto.SolutionSubmissionRequest;
import com.bajajfinserv.javaqualifier.dto.WebhookGenerationRequest;
import com.bajajfinserv.javaqualifier.dto.WebhookGenerationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ApplicationTaskRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationTaskRunner.class);
    private final RestTemplate restTemplate;

    // API endpoint for generating the webhook
    private static final String GENERATE_WEBHOOK_URL = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

    public ApplicationTaskRunner(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void run(String... args) {
        try {
            [cite_start]// Step 1: Generate the webhook [cite: 4]
            WebhookGenerationResponse webhookResponse = generateWebhook();

            if (webhookResponse != null && webhookResponse.getWebhookUrl() != null && webhookResponse.getAccessToken() != null) {
                String webhookUrl = webhookResponse.getWebhookUrl();
                String accessToken = webhookResponse.getAccessToken();
                logger.info("✅ Webhook generated successfully. URL: {}", webhookUrl);

                [cite_start]// Step 2: Get the predefined SQL query [cite: 5]
                String sqlQuery = getSqlQuery();

                [cite_start]// Step 3: Submit the solution to the webhook URL [cite: 6]
                submitSolution(webhookUrl, accessToken, sqlQuery);
            } else {
                logger.error("❌ Failed to generate webhook or response was invalid.");
            }
        } catch (Exception e) {
            logger.error("An error occurred during the process: {}", e.getMessage(), e);
        }
    }

    private WebhookGenerationResponse generateWebhook() {
        logger.info("🚀 Step 1: Generating webhook...");

        [cite_start]// Create the request body [cite: 11, 12, 13, 14]
        WebhookGenerationRequest requestBody = new WebhookGenerationRequest(
                "John Doe",
                "REG12347",
                "john@example.com"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<WebhookGenerationRequest> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<WebhookGenerationResponse> response = restTemplate.postForEntity(
                    GENERATE_WEBHOOK_URL,
                    entity,
                    WebhookGenerationResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                logger.error("Failed to generate webhook. Status: {}, Body: {}", response.getStatusCode(), response.getBody());
                return null;
            }
        } catch (Exception e) {
            logger.error("Exception during webhook generation: {}", e.getMessage());
            return null;
        }
    }

    /**
     * This method now returns a hardcoded SQL query solution directly,
     * as the dynamic check is no longer needed.
     */
    private String getSqlQuery() {
        logger.info("🤔 Step 2: Preparing the predefined SQL query...");

        // The query solves the problem of finding the highest salary not on the 1st of the month,
        [cite_start]// along with the employee's name, age, and department. [cite: 81, 82]
        String finalQuery = "SELECT P.AMOUNT AS SALARY, CONCAT(E.FIRST_NAME, ' ', E.LAST_NAME) AS NAME, TIMESTAMPDIFF(YEAR, E.DOB, CURRENT_DATE) AS AGE, D.DEPARTMENT_NAME FROM PAYMENTS P JOIN EMPLOYEE E ON P.EMP_ID = E.EMP_ID JOIN DEPARTMENT D ON E.DEPARTMENT = D.DEPARTMENT_ID WHERE EXTRACT(DAY FROM P.PAYMENT_TIME) != 1 ORDER BY P.AMOUNT DESC LIMIT 1;";
        
        logger.info("📝 SQL Query prepared.");
        return finalQuery;
    }

    private void submitSolution(String webhookUrl, String accessToken, String sqlQuery) {
        logger.info("📤 Step 3: Submitting the solution...");

        [cite_start]// Create the request body [cite: 30, 31]
        SolutionSubmissionRequest requestBody = new SolutionSubmissionRequest(sqlQuery);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); [cite_start]// [cite: 28]
        [cite_start]// Set the JWT token in the Authorization header [cite: 27]
        headers.setBearerAuth(accessToken);

        HttpEntity<SolutionSubmissionRequest> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(webhookUrl, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                logger.info("🎉 Solution submitted successfully! Response: {}", response.getBody());
            } else {
                logger.error("❌ Failed to submit solution. Status: {}, Body: {}", response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            logger.error("Exception during solution submission: {}", e.getMessage());
        }
    }
}