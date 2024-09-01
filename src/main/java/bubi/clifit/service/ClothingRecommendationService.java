package bubi.clifit.service;

import bubi.clifit.controller.dto.WeatherResponse;
import bubi.clifit.domain.Image;
import bubi.clifit.domain.ImageRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ClothingRecommendationService {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private OpenAiService openAiService;

    @Autowired
    private ImageRepository imageRepository;

    public List<Image> recommendClothing() {
        // 날씨 정보 가져오기
        WeatherResponse weatherInfo = weatherService.getWeather();
        String prompt = generatePrompt(weatherInfo);

        // OpenAI API로 추천 결과 받기
        String aiResponse = openAiService.getRecommendation(prompt);
        System.out.println("AI Response: " + aiResponse);

        // 추천된 옷 카테고리별 필터링
        List<Image> recommendations = parseAiResponse(aiResponse, weatherInfo);
        System.out.println("Recommendations: " + recommendations);

        return recommendations;
    }

    private String generatePrompt(WeatherResponse weatherInfo) {
        return String.format("%s도 %s 날씨를 기준으로 계절 %s에 맞는 코디 추천",
                weatherInfo.getTemperature(), weatherInfo.getWeatherCondition(), weatherInfo.getSeason());
    }

    private List<Image> parseAiResponse(String aiResponse, WeatherResponse weatherInfo) {
        List<Image> additionalRecommendations = new ArrayList<>();

        if (aiResponse != null && !aiResponse.isEmpty()) {
            try {
                String content = extractContentFromResponse(aiResponse);
                System.out.println("Extracted Content: " + content);

                // 항목을 정리 및 나누기
                String[] items = content.split("\n");
                System.out.println("Extracted Items: " + Arrays.toString(items));

                for (String item : items) {
                    item = item.trim().replaceAll("[^a-zA-Z0-9가-힣\\s]", ""); // 특수 문자 제거
                    System.out.println("Searching for item: " + item);
                    Image image = imageRepository.findByTypeAndWeatherAndSeason(item, weatherInfo.getWeatherCondition(), weatherInfo.getSeason());
                    if (image != null) {
                        additionalRecommendations.add(image);
                    } else {
                        System.out.println("No image found for item: " + item);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to parse AI response", e);
            }
        } else {
            System.out.println("AI response is empty or null.");
        }

        return additionalRecommendations;
    }


    private String extractContentFromResponse(String aiResponse) {
        // JSON 응답 파싱
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(aiResponse);
            JsonNode choicesNode = rootNode.path("choices");

            if (choicesNode.isArray() && choicesNode.size() > 0) {
                JsonNode messageNode = choicesNode.get(0).path("message");
                return messageNode.path("content").asText().trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String[] extractRecommendations(String content) {
        // 추천 항목 추출 로직 수정
        String cleanedContent = content.replaceAll("\\d+\\.", "").trim(); // 항목 번호 제거
        return cleanedContent.split("\n"); // 줄바꿈으로 구분된 항목
    }
}