package com.example.film_distribution_system.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.film_distribution_system.repositories.MovieRepository;
import com.example.film_distribution_system.repositories.MovieSpecification;
import com.example.film_distribution_system.models.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public List<String> getCountries() {
        return movieRepository.findAllCountries();
    }

    public List<String> getGenres() {
        return movieRepository.findAllGenres();
    }

    public List<Movie> listDBMovies(String keyword, String genre, String country, Integer year) {

        Specification<Movie> filter = Specification.where(MovieSpecification.byYear(year)).and(MovieSpecification.byCountry(country)).and(MovieSpecification.byGenre(genre)).and(MovieSpecification.search(keyword));
        return movieRepository.findAll(filter);
    }


    public List<Movie> listFSMovies(String keyword, String genre, String country, Integer year, Boolean desc) {

        Specification<Movie> filter = Specification.where(MovieSpecification.byYear(year)).and(MovieSpecification.byCountry(country)).and(MovieSpecification.byGenre(genre)).and(MovieSpecification.search(keyword));

        if(desc){return movieRepository.findAll(filter, Sort.by(Sort.Direction.DESC, "rating"));}
        else {return movieRepository.findAll(filter, Sort.by(Sort.Direction.ASC, "rating"));}
    }

    public void save(Movie movie) {
        this.movieRepository.save(movie);
    }

    public Movie findById(Long id) {
        return movieRepository.findById(id).get();
    }

    public void delete(Long id) {
        movieRepository.deleteById(id);
    }

//    Аналитика

    public Map<Integer, Double> getAverageRatingByYear() {
        List<Object[]> results = movieRepository.getAverageRatingByYear();
        Map<Integer, Double> averageRatingsByYear = new HashMap<>();
        for (Object[] result : results) {
            Integer year = (Integer) result[0];
            Double averageRating = (Double) result[1];
            averageRatingsByYear.put(year, averageRating);
        }
        return averageRatingsByYear;
    }

    public Map<String, Double> getAverageRatingByCountry() {
        List<Object[]> results = movieRepository.getAverageRatingByCountry();
        Map<String, Double> averageRatingsByCountry = new HashMap<>();
        for (Object[] result : results) {
            String country = (String) result[0];
            Double averageRating = (Double) result[1];
            averageRatingsByCountry.put(country, averageRating);
        }
        return averageRatingsByCountry;
    }

    public Map<String, Double> getAverageRatingByGenre() {
        List<Object[]> results = movieRepository.getAverageRatingByGenre();
        Map<String, Double> averageRatingsByGenre = new HashMap<>();
        for (Object[] result : results) {
            String genre = (String) result[0];
            Double averageRating = (Double) result[1];
            averageRatingsByGenre.put(genre, averageRating);
        }
        return averageRatingsByGenre;
    }

    public List<Movie> getTop5ByGenre(String genre) {
        return movieRepository.findTop5ByGenreOrderByRatingDesc(genre);
    }

}
