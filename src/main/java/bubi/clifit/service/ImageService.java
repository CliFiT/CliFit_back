package bubi.clifit.service;

import bubi.clifit.domain.Image;
import bubi.clifit.domain.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ImageService {

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image saveImage(MultipartFile file, String category, String type, String season,
                           String weather,String color , double minTemperature, double maxTemperature) throws IOException {
        Image image = new Image();
        image.setCategory(category);
        image.setType(type);
        image.setSeason(season);
        image.setWeather(weather);
        image.setColor(color);
        image.setMinTemperature(minTemperature);
        image.setMaxTemperature(maxTemperature);
        image.setContentType(file.getContentType());
        image.setData(file.getBytes());

        //이미지 관련 정보 설정
        image.setData(file.getBytes());
        return imageRepository.save(image);
    }

    public Optional<Image> getImage(Long id) {
        return imageRepository.findById(id);
    }

}
