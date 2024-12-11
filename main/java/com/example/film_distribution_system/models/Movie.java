package com.example.film_distribution_system.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String image;
    private String title;
    private String genre;
    @Lob
    private String description;
    private int duration;
    private Double rating;
    private int year;
    private String country;
    private int price;

}
