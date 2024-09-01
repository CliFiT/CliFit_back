package bubi.clifit.controller;

import bubi.clifit.domain.Image;
import bubi.clifit.service.ClothingRecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/image")
public class ClothingRecommendationController {

    @Autowired
    private ClothingRecommendationService recommendationService;

    @GetMapping("/recommend")
    public List<Image> getRecommendations() {
        return recommendationService.recommendClothing();
    }
}
