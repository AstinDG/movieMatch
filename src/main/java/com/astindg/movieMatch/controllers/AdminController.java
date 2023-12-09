package com.astindg.movieMatch.controllers;

import com.astindg.movieMatch.services.MovieService;
import com.astindg.movieMatch.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final MovieService movieService;

    @Autowired
    public AdminController(UserService userService, MovieService movieService) {
        this.userService = userService;
        this.movieService = movieService;
    }

    @GetMapping()
    public String index(Model model){
        Integer usersAmount = userService.getUsersAmount();
        Integer countMessages = userService.getMessageCount(5);
        Integer moviesAmount = movieService.getMoviesAmount();

        model.addAttribute("usersAmount", usersAmount);
        model.addAttribute("countMessages", countMessages);
        model.addAttribute("moviesAmount", moviesAmount);

        return "admin";
    }

}
