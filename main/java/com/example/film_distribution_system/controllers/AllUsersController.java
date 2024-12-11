package com.example.film_distribution_system.controllers;

import java.util.List;
import java.util.stream.Collectors;

import com.example.film_distribution_system.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class AllUsersController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/user/main")
    public String showHomePage(Model model, @RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "genre", required = false) String genre, @RequestParam(value = "country", required = false) String country,
                               @RequestParam(value = "year", required = false) Integer year, @RequestParam(value = "sort", required = false) String sort,
                               @AuthenticationPrincipal UserDetails userDetails) {

        boolean isAuthenticated = userDetails != null;
        model.addAttribute("isAuthenticated", isAuthenticated);

        if (isAuthenticated) {
            String role = getRole(userDetails);
            model.addAttribute("role", role);
        }

        Boolean desc;
        if (sort != null) {
            desc = !sort.equals("rating_asc");
        }else {desc = true;}

        model.addAttribute("countries", movieService.getCountries());
        model.addAttribute("genres", movieService.getGenres());
        model.addAttribute("listMovies", movieService.listFSMovies(keyword, genre, country, year, desc));

        return "users/home_page";
    }

    @GetMapping("/")
    public String info(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        boolean isAuthenticated = userDetails != null;
        model.addAttribute("isAuthenticated", isAuthenticated);

        if (isAuthenticated) {
            String role = getRole(userDetails);
            model.addAttribute("role", role);
        }

        return "users/author";
    }


    private String getRole(UserDetails userDetails) {
        if (userDetails != null) {
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority().replace("ROLE_", ""))
                    .collect(Collectors.toList());

            return roles.contains("ADMIN") ? "ADMIN" : "USER";
        }
        return "null";
    }
}
