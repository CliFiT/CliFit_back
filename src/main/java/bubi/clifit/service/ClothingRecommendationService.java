package bubi.clifit.service;

import bubi.clifit.controller.dto.WeatherResponse;
import bubi.clifit.domain.Image;
import bubi.clifit.domain.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClothingRecommendationService {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    OpenAiService openAiService;

    @Autowired
    private ImageRepository imageRepository;

    public List<Image> recommendClothing() {
        //날씨 정보 가져오기
        WeatherResponse weatherInfo = weatherService.getWeather();
        String prompt = generatePrompt(weatherInfo);

        //OpenAI API로 추천 결과 받기
        String aiResponse = openAiService.getRecommendation(prompt);

        //추천된 옷 카테고리별 필터링
        List<Image> recommendations = new ArrayList<>();

        //AI 응답으로부터 추천 항목 추출
        recommendations.addAll(parseAiResponse(aiResponse, weatherInfo));

        return recommendations;
    }

    private String generatePrompt(WeatherResponse weatherInfo) {
        return String.format("%s도, 습도 %s의 날씨를 기준으로 계절 %s에 맞는 코디 추천",
                weatherInfo.getTemperature(), weatherInfo.getHumidity(), weatherInfo.getSeason(), weatherInfo.getWeatherCondition());
    }

    private List<Image> parseAiResponse(String aiResponse, WeatherResponse weatherInfo) {
        List<Image> additionalRecommendations = new ArrayList<>();

        if(aiResponse != null && !aiResponse.isEmpty()) {
            String[] items = aiResponse.split(",\\s*");

            for(String item : items) {
                Image image = imageRepository.findByTypeAndWeatherAndSeason(item,weatherInfo.getWeatherCondition(),weatherInfo.getSeason());
                if(image != null) {
                    additionalRecommendations.add(image);
                }
            }
        }

        return additionalRecommendations;
    }

}
