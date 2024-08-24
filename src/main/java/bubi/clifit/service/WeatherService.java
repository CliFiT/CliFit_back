package bubi.clifit.service;

import bubi.clifit.controller.dto.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class WeatherService {

    private final RestTemplate restTemplate;

    @Value("${weather.api.url}")
    private String apiUrl;

    @Value("${weather.api.authKey}")
    private String authKey;

    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public WeatherResponse getWeather() {
        try {
            String baseDate = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
            String baseTime = calculateBaseTime();

            String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .queryParam("pageNo", 1)
                    .queryParam("numOfRows", 12)
                    .queryParam("dataType", "XML")
                    .queryParam("base_date", baseDate)
                    .queryParam("base_time", baseTime)
                    .queryParam("nx", 98)
                    .queryParam("ny", 76)
                    .queryParam("authKey", authKey)  // authKey 대신 serviceKey로 사용하는 경우도 있음
                    .toUriString();

            String xmlResponse = restTemplate.getForObject(url, String.class);

            if (xmlResponse == null) {
                throw new RuntimeException("API response is null");
            }

            return parseXmlToWeatherResponse(xmlResponse);

        } catch (RestClientException e) {
            // 로그를 기록하거나 예외를 처리
            throw new RuntimeException("Failed to fetch weather data", e);
        } catch (Exception e) {
            // XML 파싱 또는 기타 오류 처리
            throw new RuntimeException("Error processing weather data", e);
        }
    }

    private String calculateBaseTime() {
        LocalTime now = LocalTime.now();
        if(now.isBefore(LocalTime.of(2,0))) {
            return "2300";
        } else if(now.isBefore(LocalTime.of(5,0))) {
            return "0200";
        } else if(now.isBefore(LocalTime.of(8,0))) {
            return "0500";
        } else if(now.isBefore(LocalTime.of(11,0))) {
            return "0800";
        } else if(now.isBefore(LocalTime.of(14,0))) {
            return "1100";
        } else if(now.isBefore(LocalTime.of(17,0))) {
            return "1400";
        } else if(now.isBefore(LocalTime.of(20,0))) {
            return "1700";
        } else if(now.isBefore(LocalTime.of(23,0))) {
            return "2000";
        } else {
            return "2300";
        }
    }

    private WeatherResponse parseXmlToWeatherResponse(String xml) {
        WeatherResponse response = new WeatherResponse();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xml)));
            doc.getDocumentElement().normalize();

            NodeList items = doc.getElementsByTagName("item");

            for (int i = 0; i < items.getLength(); i++) {
                Node node = items.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String category = element.getElementsByTagName("category").item(0).getTextContent();
                    String fcstValue = element.getElementsByTagName("fcstValue").item(0).getTextContent();

                    // 카테고리와 값 출력
                    System.out.println("Category: " + category + ", Value: " + fcstValue);

                    switch (category) {
                        case "TMP":
                            response.setTemperature(fcstValue);
                            break;
                        case "REH":
                            response.setHumidity(fcstValue);
                            break;
                        case "PTY":
                            response.setWeatherCondition(mapWeatherCodeToDescription(fcstValue));
                            break;
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return response;
    }
    private String mapWeatherCodeToDescription(String code) {
        switch(code) {
            case "0" :
                return "맑음";
            case "1" :
                return "비";
            case "2" :
                return "눈";
            case "3" :
                return "소나기";
            case "4" :
                return "흐림";
            default:
                return "정보 없음";
        }
    }
}
