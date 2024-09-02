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
import java.util.stream.Collectors;

@Service
public class ClothingRecommendationService {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private OpenAiService openAiService;

    @Autowired
    private ImageRepository imageRepository;

    public String recommendClothing() {
        // 날씨 정보 가져오기
        WeatherResponse weatherInfo = weatherService.getWeather();

        // DB에서 모든 옷 정보 가져오기
        List<Image> allImages = imageRepository.findAll();

        // 프롬프트 생성
        String prompt = generatePrompt(weatherInfo, allImages);

        // OpenAI API로 추천 결과 받기
        String aiResponse = openAiService.getRecommendation(prompt);
        System.out.println("AI Response: " + aiResponse);

        // 추천된 옷 카테고리별 필터링
        String recommendations = parseAiResponse(aiResponse, allImages);
        System.out.println("Recommendations: " + recommendations);

        return recommendations;
    }

    private String generatePrompt(WeatherResponse weatherInfo, List<Image> allImages) {
        // DB에서 가져온 옷 정보를 프롬프트에 포함시키기
        String clothingOptions = allImages.stream()
                .map(image -> image.getType() + image.getColor()) // 옷의 타입을 가져와서
                .distinct() // 중복 제거
                .collect(Collectors.joining(", ")); // 콤마로 구분된 문자열 생성

        return String.format("다음의 옷들 중에서 %s도 %s 날씨를 기준으로 계절 %s에 맞는 코디를 추천해 주세요: %s 공백 포함 160자 이내 최대 2문장으로 줘",
                weatherInfo.getTemperature(), weatherInfo.getWeatherCondition(), weatherInfo.getSeason(), clothingOptions);
    }

    private String parseAiResponse(String aiResponse, List<Image> allImages) {
        String additionalRecommendations = new String();

        if (aiResponse != null && !aiResponse.isEmpty()) {
            try {
                String content = extractContentFromResponse(aiResponse);
                System.out.println("Extracted Content: " + content);

                // 항목을 정리 및 나누기
                String[] items = content.split("\n");
                System.out.println("Extracted Items: " + Arrays.toString(items));

                // DB에서 가져온 옷들 중에서 추천된 옷만 필터링
                List<String> itemTypes = Arrays.stream(items)
                        .map(String::trim)
                        .map(item -> item.replaceAll("[^a-zA-Z0-9가-힣\\s]", "")) // 특수 문자 제거
                        .distinct()
                        .collect(Collectors.toList());

                additionalRecommendations = content;

                if (additionalRecommendations.isEmpty()) {
                    System.out.println("No matching images found.");
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
}
