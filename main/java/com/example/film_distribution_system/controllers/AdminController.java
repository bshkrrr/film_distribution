package com.example.film_distribution_system.controllers;

import com.example.film_distribution_system.models.Movie;
import com.example.film_distribution_system.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/edit/{id}")
    public ModelAndView editMovie(@PathVariable Long id) {
        Movie movie = movieService.findById(id);
        ModelAndView modelAndView = new ModelAndView("admin/edit_movie");
        modelAndView.addObject("movie", movie);
        return modelAndView;
    }

    @RequestMapping("/delete/{id}")
    public String deleteMovie(@PathVariable Long id) {
        movieService.delete(id);
        return "redirect:/admin/database";
    }

    @GetMapping("/database")
    public String showHomePage(Model model, @RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "genre", required = false) String genre, @RequestParam(value = "country", required = false) String country,
                               @RequestParam(value = "year", required = false) Integer year) {


        model.addAttribute("countries", movieService.getCountries());
        model.addAttribute("genres", movieService.getGenres());
        model.addAttribute("listMovies", movieService.listDBMovies(keyword, genre, country, year));

        return "admin/database";
    }

    @GetMapping("/new")
    public String NewMovie(Model model) {
        model.addAttribute("movie", new Movie());
        return "admin/new_movie";
    }

    @PostMapping("/save")
    public String saveMovie(@ModelAttribute("movie") Movie movie,
                            @RequestParam(value = "image_file", required = false) MultipartFile imageFile) throws IOException {

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageName = imageFile.getOriginalFilename();
            imageFile.transferTo(new File("/Users/bshkrrr/IdeaProjects/kursach_spring/src/main/resources/static/images/" + imageName));
            movie.setImage(imageName);
        } else {
            if (movie.getId() != null) {
                Movie existingMovie = movieService.findById(movie.getId());
                if (existingMovie != null && existingMovie.getImage() != null) {
                    movie.setImage(existingMovie.getImage());
                }
            }
        }

        movieService.save(movie);
        return "redirect:/admin/database";
    }

    @GetMapping("/analytics")
    public String analytics(@RequestParam(name = "genre", required = false, defaultValue = "Боевик") String genre, Model model) {

        model.addAttribute("averageRatingByGenre", movieService.getAverageRatingByGenre());
        model.addAttribute("averageRatingByYear", movieService.getAverageRatingByYear());
        model.addAttribute("averageRatingByCountry", movieService.getAverageRatingByCountry());

        List<Movie> topMovies = movieService.getTop5ByGenre(genre);
        Movie topMovie = topMovies.isEmpty() ? null : topMovies.get(0);

        if (topMovies.isEmpty()) {
            model.addAttribute("message", "Нет фильмов для выбранного жанра.");
        }

        model.addAttribute("genres", movieService.getGenres());
        model.addAttribute("topMovies", topMovies);
        model.addAttribute("topMovie", topMovie);


        return "admin/analytics";
    }

}
