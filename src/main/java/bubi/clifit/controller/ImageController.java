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
    public ResponseEntity<String> upload(@RequestPart("image") MultipartFile file) {
        try {
            Image image = imageService.saveImage(file);
            return ResponseEntity.ok("이미지 업로드 성공!: " + image.getId());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("업로드 실패: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable long id) {
        Optional<Image> imageOptional = imageService.getImage(id);

        if (imageOptional.isPresent()) {
            Image image = imageOptional.get();
            String contentType = image.getType(); // 이미지 MIME 타입 (예: image/jpeg)
            String fileName = image.getName(); // 이미지 파일명 (예: image.jpg)

            return ResponseEntity.ok()
                    .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                    .body(image.getData());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
