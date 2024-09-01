package bubi.clifit.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAiService {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    private final RestTemplate restTemplate;

    public OpenAiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getRecommendation(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openAiApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 본문 생성 (JSON 형식)
        String requestBody = String.format(
                "{\"model\": \"gpt-4\", \"messages\": [{\"role\": \"user\", \"content\": \"%s\"}], \"max_tokens\": 150}",
                prompt
        );

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    "https://api.openai.com/v1/chat/completions",
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                throw new RuntimeException("Failed to get recommendation from OpenAI API: " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            throw new RuntimeException("Error while calling OpenAI API: " + e.getMessage(), e);
        }
    }
}