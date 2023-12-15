package br.com.tce.carsystem.domain.car;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

@Entity(name = "car")
@Table(name = "car")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Car implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "licensePlate", unique = true, nullable = false)
    private String licensePlate;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "counter")
    private Integer counter;

    @Lob
    @Column(name = "image")
    private byte[] image;

    public String getImage() {
        if (image != null) {
            return new String(image, StandardCharsets.UTF_8);
        }
        return null;
    }

    @Column(name = "user_id")
    private String userId;

    @PrePersist
    public void prePersist() {
        counter = 1;
    }
}
