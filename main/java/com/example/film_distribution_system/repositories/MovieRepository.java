package com.example.film_distribution_system.repositories;

import java.util.List;

import com.example.film_distribution_system.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {
    @Query("SELECT p FROM Movie p WHERE CONCAT(p.id, p.title, p.genre, p.duration, p.rating, p.year, p.country, p.price) LIKE %?1%")
    List<Movie> searchMovies(@Param("keyword") String keyword);

//  Аналитика
    @Query("SELECT m.year, AVG(m.rating) FROM Movie m GROUP BY m.year")
    List<Object[]> getAverageRatingByYear();

    @Query("SELECT m.country, AVG(m.rating) FROM Movie m GROUP BY m.country")
    List<Object[]> getAverageRatingByCountry();

    @Query("SELECT m.genre, AVG(m.rating) FROM Movie m GROUP BY m.genre")
    List<Object[]> getAverageRatingByGenre();

    @Query("SELECT DISTINCT m.country FROM Movie m")
    List<String> findAllCountries();

    @Query("SELECT DISTINCT m.genre FROM Movie m")
    List<String> findAllGenres();

    List<Movie> findTop5ByGenreOrderByRatingDesc(String genre);
}