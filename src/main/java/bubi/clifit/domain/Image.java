package bubi.clifit.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;
    private String type;
    private String season;
    private String weather;
    private double minTemperature;
    private double maxTemperature;

    @Lob
    @Column(name = "data", columnDefinition = "LONGBLOB")
    private byte[] data;

}
