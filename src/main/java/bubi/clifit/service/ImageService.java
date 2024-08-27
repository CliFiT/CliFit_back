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

    public Image saveImage(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getOriginalFilename());
        image.setType(file.getContentType());
        image.setData(file.getBytes());

        return imageRepository.save(image);
    }

    public Optional<Image> getImage(Long id) {
        return imageRepository.findById(id);
    }

}
