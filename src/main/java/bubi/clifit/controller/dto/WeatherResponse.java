package bubi.clifit.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class WeatherResponse {

    private String temperature; //온도
    private String humidity; //습도
    private String weatherCondition;

    public WeatherResponse() {
    }

    //모든 필드를 초기화하는 생성자
    public WeatherResponse(String temperature, String humidity, String weatherCondition) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.weatherCondition = weatherCondition;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    //디버깅 또는 로깅을 위한 toString 메서드
    @Override
    public String toString() {
        return "WeatherResponse{" +
                "temperature='" + temperature +'\'' +
                ", humidity='" + humidity + '\'' +
                ", weatherCondition='" + weatherCondition + '\'' +
                '}';
    }
}
