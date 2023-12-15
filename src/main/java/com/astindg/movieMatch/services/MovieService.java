package com.astindg.movieMatch.services;

import com.astindg.movieMatch.model.Language;
import com.astindg.movieMatch.model.Movie;
import com.astindg.movieMatch.model.MovieDetails;
import com.astindg.movieMatch.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    /*public List<Movie> findAll(){

        List<Movie> movieList = new ArrayList<>();

        Movie movie = new Movie();
        MovieDetailsEn en = new MovieDetailsEn(
                "Passengers",
                "Science friction",
                "The spaceship, Starship Avalon, in its 120-year voyage to a distant colony planet known as the \"Homestead Colony\" and transporting 5,258 people has a malfunction in one of its sleep chambers. As a result one hibernation pod opens prematurely and the one person that awakes, Jim Preston (Chris Pratt) is stranded on the spaceship, still 90 years from his destination.",
                "./src/main/resources/img/movie/en/Passengers.jpg"
        );
        en.setMovie(movie);
        MovieDetailsUa ua = new MovieDetailsUa(
                "Пробудження",
                "Наукова фантастика",
                "Космічний корабель із 5000 пасажирів на борту, завантажених під час полету в штучний сон, тримає курс на планету, яка стане для них новим домом. Все іде по плану, але із-за своєї системи двоє пасажирів неожиданно просипаються і розуміють, що до кінцевого пункту подорожі - 90 років шляху",
                "./src/main/resources/img/movie/ua/Passengers.jpg"
        );
        ua.setMovie(movie);
        MovieDetailsRu ru = new MovieDetailsRu(
                "Пассажиры",
                "Научная фантастика",
                "Космический корабль с 5,000 пассажиров на борту, погруженных на время полета в искусственный сон, держит курс на планету, которая станет для них новым домом. Всё идет по плану, но из-за сбоя системы двое пассажиров неожиданно просыпаются и понимают, что до конечного пункта путешествия - 90 лет пути",
                "./src/main/resources/img/movie/ru/Passengers.jpg"
        );
        ru.setMovie(movie);

        movie.setYearOfRelease(2016);
        movie.setDetailsEn(en);
        movie.setDetailsUa(ua);
        movie.setDetailsRu(ru);

        movieList.add(movie);

        movie = new Movie();
        en = new MovieDetailsEn(
                "Intouchables",
                "Comedy",
                "After he becomes a quadriplegic from a paragliding accident, an aristocrat hires a young man from the projects to be his caregiver. After he becomes a quadriplegic from a paragliding accident, an aristocrat hires a young man from the projects to be his caregiver.",
                "./src/main/resources/img/movie/en/intouchables.jpg"
        );
        en.setMovie(movie);
        ua = new MovieDetailsUa(
                "1+1 / Недоторканні",
                "Комедія",
                "Заснована на реальних подіях історія незвичайної дружби двох абсолютно несхожих один на одного чоловіків: прикутого до інвалідного крісла багатого аристократа і тільки що звільнившогося з в'язниці молодого араба ...",
                "./src/main/resources/img/movie/ua/intouchables.jpg"
        );
        ua.setMovie(movie);
        ru = new MovieDetailsRu(
                "1+1 / Неприкасаемые",
                "Комедия",
                "Парализованный аристократ Филипп нанимает себе в помощники темнокожего Дрисса, хотя тот совершенно не подходит на роль сиделки. Но именно новый друг помогает Филиппу снова почувствовать вкус к жизни.",
                "./src/main/resources/img/movie/ru/intouchables.jpg"
        );
        ru.setMovie(movie);

        movie.setYearOfRelease(2011);
        movie.setDetailsEn(en);
        movie.setDetailsUa(ua);
        movie.setDetailsRu(ru);

        movieList.add(movie);

        movie = new Movie();

        en = new MovieDetailsEn(
                "Once Upon A Time In The West",
                "Western",
                "Story of a young woman, Mrs. McBain, who moves from New Orleans to frontier Utah, on the very edge of the American West. She arrives to find her new husband and family slaughtered, but by whom? The prime suspect, coffee-lover Cheyenne, befriends her and offers to go after the real killer, assassin gang leader Frank, in her honor. He is accompanied by Harmonica, a man already on a quest to get even.",
                "./src/main/resources/img/movie/en/Once-upon-a-time-in-the-west.jpg"
        );
        en.setMovie(movie);
        ua = new MovieDetailsUa(
                "Одного разу на Дикому Заході",
                "Вестерн",
                "\"Одного разу на Дикому Заході\" - класичний вестерн, що розповідає історію таємничого незнайомця, який приїжджає до маленького містечка, де зустрічає вродливу вдову і втягується у смертельний конфлікт з безжальною бандою злочинців.",
                "./src/main/resources/img/movie/ua/Once-upon-a-time-in-the-west.jpg"
        );
        ua.setMovie(movie);
        ru = new MovieDetailsRu(
                "Однажды на Диком Западе",
                "Вестерн",
                "Злобный бизнесмен нанимает киллера, чтобы тот убил вдову, отказавшуюся продавать ему землю. Но женщину берётся защищать загадочный стрелок по прозвищу Гармоника.",
                "./src/main/resources/img/movie/ru/Once-upon-a-time-in-the-west.jpg"
        );
        ru.setMovie(movie);
        movie.setYearOfRelease(1968);
        movie.setDetailsEn(en);
        movie.setDetailsUa(ua);
        movie.setDetailsRu(ru);

        movieList.add(movie);


        movieRepository.saveAll(movieList);
        return movieList;
    }*/

    public Optional<Movie> findById(Integer id) {
        return movieRepository.findById(id);
    }

    @Transactional
    public void saveDetails(Integer id, MovieDetails movieDetails, MultipartFile multipartFile, Language language) {
        Movie movie = findById(id).orElse(null);
        if (movie == null) {
            return;
        }
        MovieDetails actualDetails = movie.getMovieDetails(language);
        checkDescriptionAndSave(movieDetails, actualDetails);
        checkNameAndSave(movieDetails, actualDetails);
        checkGenreAndSave(movieDetails, actualDetails);

        if (multipartFile != null) {
            byte[] newImageBytes = null;
            byte[] currentImageBytes = null;
            try {
                newImageBytes = multipartFile.getBytes();
                currentImageBytes = Files.readAllBytes(actualDetails.getImage().toPath());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            if (newImageBytes != null) {
                String path = buildPath(language, multipartFile.getOriginalFilename());
                if (currentImageBytes != null) {
                    if (!Arrays.equals(newImageBytes, currentImageBytes)) {
                        actualDetails.getImage().delete();

                        saveNewImage(actualDetails, path, newImageBytes);
                    }
                } else {
                    saveNewImage(actualDetails, path, newImageBytes);
                }
            }
        }

        this.movieRepository.save(movie);
    }

    private String buildPath(Language language, String fileName){
        StringBuilder pathSB = new StringBuilder();
        pathSB.append("./src/main/resources/img/movie/");
        switch (language) {
            case EN -> pathSB.append("en/");
            case UA -> pathSB.append("ua/");
            case RU -> pathSB.append("ru/");
        }
        pathSB.append(fileName);
        return pathSB.toString();
    }

    private void saveNewImage(MovieDetails details, String path, byte[] newImageBytes){
        File newImage = new File(path);

        try (OutputStream os = new FileOutputStream(newImage)) {
            os.write(newImageBytes);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        details.setImagePath(path);
    }
    private void checkDescriptionAndSave(MovieDetails movieDetails, MovieDetails actualDetails){
        if (!movieDetails.getDescription().isEmpty()) {
            if (!movieDetails.getDescription().equals(actualDetails.getDescription())) {
                actualDetails.setDescription(movieDetails.getDescription());
            }
        }
    }
    private void checkNameAndSave(MovieDetails movieDetails, MovieDetails actualDetails){
        if (!movieDetails.getName().isEmpty()) {
            if (!movieDetails.getName().equals(actualDetails.getName())) {
                actualDetails.setName(movieDetails.getName());
            }
        }
    }
    private void checkGenreAndSave(MovieDetails movieDetails, MovieDetails actualDetails){
        if (!movieDetails.getGenre().isEmpty()) {
            if (!movieDetails.getGenre().equals(actualDetails.getGenre())) {
                actualDetails.setGenre(movieDetails.getGenre());
            }
        }
    }

    public Integer getMoviesAmount() {
        return movieRepository.getMoviesAmount();
    }

    public void updateYearRelease(Integer movieId, Integer updatedYearRelease) {
        Optional<Movie> foundMovie = findById(movieId);
        if (foundMovie.isEmpty()) {
            return;
        }
        Movie movie = foundMovie.get();
        movie.setYearOfRelease(updatedYearRelease);
        this.movieRepository.save(movie);
    }
}
