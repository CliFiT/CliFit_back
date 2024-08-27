package bubi.clifit.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.awt.*;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
