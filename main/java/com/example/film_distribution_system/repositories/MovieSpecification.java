package com.example.film_distribution_system.repositories;

import com.example.film_distribution_system.models.Movie;
import org.springframework.data.jpa.domain.Specification;

public final class MovieSpecification {

    private MovieSpecification() {}

    public static Specification<Movie> byYear(Integer year) {

        return (root, query, cb) -> {
            if(year == null) return null;
            return cb.equal(root.get("year"), year);
        };
    }

    public static Specification<Movie> byGenre(String genre) {

        return (root, query, cb) -> {
            if(genre == null || genre.isEmpty()) return null;
            return cb.equal(root.get("genre"), genre);
        };
    }

    public static Specification<Movie> byCountry(String country) {

        return (root, query, cb) -> {
            if(country == null || country.isEmpty()) return null;
            return cb.equal(root.get("country"), country);
        };
    }

    public static Specification<Movie> search(String keyword) {

        return (root, query, cb) -> {
            if (keyword == null || keyword.isEmpty()) return null;

            String likeKeyword = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("title")), likeKeyword),
                    cb.like(cb.lower(root.get("country")), likeKeyword),
                    cb.like((root.get("description")), likeKeyword),
                    cb.like(cb.lower(root.get("genre")), likeKeyword));
        };
    }
}
