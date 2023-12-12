package com.astindg.movieMatch.controllers;

import com.astindg.movieMatch.model.User;
import com.astindg.movieMatch.services.MovieService;
import com.astindg.movieMatch.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/user")
public class UserController {
    private final UserService userService;
    private final MovieService movieService;

    public UserController(UserService userService, MovieService movieService) {
        this.userService = userService;
        this.movieService = movieService;
    }

    @GetMapping
    public String all(Model model) {
        setHeaderParams(model);

        List<User> userList = userService.findAll();
        model.addAttribute("userList", userList);

        return "user/all";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Integer id, Model model) {
        setHeaderParams(model);

        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            model.addAttribute("userIsPresent", true);
        } else {
            model.addAttribute("userIsPresent", false);
        }

        return "user/show";
    }


    private void setHeaderParams(Model model) {
        Integer usersAmount = userService.getUsersAmount();
        Integer countMessages = userService.getMessageCount(5);
        Integer moviesAmount = movieService.getMoviesAmount();

        model.addAttribute("usersAmount", usersAmount);
        model.addAttribute("countMessages", countMessages);
        model.addAttribute("moviesAmount", moviesAmount);
    }
}
