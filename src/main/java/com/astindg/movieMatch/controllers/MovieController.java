package com.astindg.movieMatch.controllers;

import com.astindg.movieMatch.model.*;
import com.astindg.movieMatch.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public String show(@PathVariable("id") Integer id, Model model) {

        Optional<Movie> movie = movieService.findById(id);
        model.addAttribute("movieIsPresent", movie.isPresent());
        movie.ifPresent(value -> model.addAttribute("movie", value));
        return "movie/show";
    }

    @GetMapping("{id}/edit")
    public String edit(@PathVariable Integer id, Model model) {
        Optional<Movie> movie = this.movieService.findById(id);

        model.addAttribute("movieIsPresent", movie.isPresent());
        if (movie.isPresent()) {
            model.addAttribute("movie", movie.get());
            model.addAttribute("movieDetailsEn", movie.get().getDetailsEn());
            model.addAttribute("movieDetailsUa", movie.get().getDetailsUa());
            model.addAttribute("movieDetailsRu", movie.get().getDetailsRu());
        }
        return "movie/edit";
    }

    @PatchMapping("{id}/updateYearRelease")
    public String upDateYearRelease(@PathVariable("id") Integer id,
            @ModelAttribute("movie") Movie updatedYearReleaseMovie, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "movie/edit";
        }
        this.movieService.updateYearRelease(id, updatedYearReleaseMovie.getYearOfRelease());
        return "redirect:/admin/movie";
    }

    @PatchMapping("/{id}/updateDetailsEn")
    public String update(@ModelAttribute("movieDetailsEn") MovieDetailsEn movieDetailsEn,
                         BindingResult bindingResult,
                         @PathVariable("id") Integer id,
                         @RequestParam("imageEn") MultipartFile file) {
        if (bindingResult.hasErrors()) {
            return "movie/edit";
        }

        movieService.saveDetails(id, movieDetailsEn, file, Language.EN);
        return "redirect:/admin/movie";
    }

    @PatchMapping("/{id}/updateDetailsUa")
    public String update(@ModelAttribute("movieDetailsEn") MovieDetailsUa movieDetailsUa,
                         BindingResult bindingResult,
                         @PathVariable("id") Integer id,
                         @RequestParam("imageUa") MultipartFile file) {
        if (bindingResult.hasErrors()) {
            return "movie/edit";
        }

        movieService.saveDetails(id, movieDetailsUa, file, Language.UA);
        return "redirect:/admin/movie";
    }

    @PatchMapping("/{id}/updateDetailsRu")
    public String update(@ModelAttribute("movieDetailsEn") MovieDetailsRu movieDetailsRu,
                         BindingResult bindingResult,
                         @PathVariable("id") Integer id,
                         @RequestParam("imageRu") MultipartFile file) {
        if (bindingResult.hasErrors()) {
            return "movie/edit";
        }

        movieService.saveDetails(id, movieDetailsRu, file, Language.RU);
        return "redirect:/admin/movie";
    }
}
