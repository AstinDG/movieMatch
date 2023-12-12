package com.astindg.movieMatch.controllers;

import com.astindg.movieMatch.model.Language;
import com.astindg.movieMatch.model.Movie;
import com.astindg.movieMatch.services.MovieService;
import com.astindg.movieMatch.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("admin/movie")
public class MovieController {
    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping()
    public String all(Model model) {

        List<Movie> movieList = movieService.findAll();

        model.addAttribute("movieList", movieList);

        return "movie/all";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Integer id, Model model){

        Optional<Movie> movie = movieService.findById(id);
        if(movie.isPresent()){
            model.addAttribute("movie", movie.get());
            model.addAttribute("movieIsPresent", true);
        } else {
            model.addAttribute("movieIsPresent", false);
        }
        return "movie/show";
    }
}
