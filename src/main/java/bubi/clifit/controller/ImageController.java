package bubi.clifit.controller;

import bubi.clifit.domain.Image;
import bubi.clifit.service.ImageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/image")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("image") MultipartFile file,
                                        @RequestParam("category") String category,
                                        @RequestParam("type") String type,
                                        @RequestParam("season") String season,
                                        @RequestParam("weather") String weather,
                                        @RequestParam("minTemperature") double minTemperature,
                                        @RequestParam("maxTemperature") double maxTemperature) {
        try {
            Image savedImage = imageService.saveImage(file, category, type, season, weather, minTemperature, maxTemperature);
            return ResponseEntity.ok("이미지 업로드 성공!:" + savedImage.getId());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("업로드 실패!: "+e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable long id) {
        Optional<Image> imageOptional = imageService.getImage(id);

        if (imageOptional.isPresent()) {
            Image image = imageOptional.get();
            String fileName = "image_" + id;

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, image.getContentType())
                    .body(image.getData());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
