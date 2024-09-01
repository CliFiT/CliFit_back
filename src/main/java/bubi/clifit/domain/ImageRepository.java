package bubi.clifit.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByType(String type);
    Image findByTypeAndWeatherAndSeason(String type, String weather, String season);
}
