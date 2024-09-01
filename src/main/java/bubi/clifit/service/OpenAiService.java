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

        String requestBody = String.format("{\"prompt\": \"%s\"}", prompt);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.openai.com/v1/completions",
                HttpMethod.POST,
                entity,
                String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to get recommendation from OpenAI API");
        }
    }
}
