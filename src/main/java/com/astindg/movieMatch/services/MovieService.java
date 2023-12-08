package com.astindg.movieMatch.services;

import com.astindg.movieMatch.model.Movie;
import com.astindg.movieMatch.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> findAll(){
        return movieRepository.findAll();
    }

    /*public List<Movie> getAll(){
        List<Movie> movieList = new ArrayList<>();

        movieList.add(
                new Movie(1,
                        2016,
                        "Passengers",
                        "Пробудження",
                        "Пассажиры",
                        "Science friction",
                        "Наукова фантастика",
                        "Научная фантастика",
                        "The spaceship, Starship Avalon, in its 120-year voyage to a distant colony planet known as the \"Homestead Colony\" and transporting 5,258 people has a malfunction in one of its sleep chambers. As a result one hibernation pod opens prematurely and the one person that awakes, Jim Preston (Chris Pratt) is stranded on the spaceship, still 90 years from his destination.",
                        "Космический корабль с 5,000 пассажиров на борту, погруженных на время полета в искусственный сон, держит курс на планету, которая станет для них новым домом. Всё идет по плану, но из-за сбоя системы двое пассажиров неожиданно просыпаются и понимают, что до конечного пункта путешествия - 90 лет пути",
                        "Космічний корабель з тисячами пасажирів вирушив у далеку подорож. Головна мета - знайти планету, яка стане для мандрівників новим домом. Пережити довгий шлях можливо тільки в стані повного анабіозу, але через збій в роботі бортового комп'ютера один з пасажирів, молодий чоловік на ім'я Джим, прокидається. Розуміючи, що до пункту призначення він не долетить (до кінця подорожі - 90 років), він вибирає найпривабливішу пасажирку і виводить її зі сну. Обуренню дівчини немає меж, але перед обличчям небезпек, герої змушені шукати спільну мову ...",
                        new File("./src/main/resources/img/Passengers.jpg")
                )
        );
        movieList.add(
                new Movie(2,
                        2011,
                        "Intouchables",
                        "1+1 / Недоторканні",
                        "1+1 / Неприкасаемые",
                        "Comedy",
                        "Комедія",
                        "Комедия",
                        "In Paris, the aristocratic and intellectual Philippe is a quadriplegic millionaire who is interviewing candidates for the position of his carer, with his red-haired secretary Magalie. Out of the blue, Driss cuts the line of candidates and brings a document from the Social Security and asks Phillipe to sign it to prove that he is seeking a job position so he can receive his unemployment benefit. Philippe challenges Driss, offering him a trial period of one month to gain experience helping him. Then Driss can decide whether he would like to stay with him or not. Driss accepts the challenge and moves to the mansion, changing the boring life of Phillipe and his employees.",
                        "Заснована на реальних подіях історія незвичайної дружби двох абсолютно несхожих один на одного чоловіків: прикутого до інвалідного крісла багатого аристократа і тільки що звільнившогося з в'язниці молодого араба ...",
                        "Парализованный аристократ Филипп нанимает себе в помощники темнокожего Дрисса, хотя тот совершенно не подходит на роль сиделки. Но именно новый друг помогает Филиппу снова почувствовать вкус к жизни.",
                        new File("./src/main/resources/img/intouchables.jpg")
                )
        );
        movieList.add(
                new Movie(3,
                        2011,
                        "Once Upon A Time In The West",
                        "Однажды на Диком Западе",
                        "Одного разу на Дикому Заході",
                        "Western",
                        "Вестерн",
                        "Вестерн",
                        "Story of a young woman, Mrs. McBain, who moves from New Orleans to frontier Utah, on the very edge of the American West. She arrives to find her new husband and family slaughtered, but by whom? The prime suspect, coffee-lover Cheyenne, befriends her and offers to go after the real killer, assassin gang leader Frank, in her honor. He is accompanied by Harmonica, a man already on a quest to get even.",
                        "Дія розгортається у вигаданому місті Флегстон на Дикому Заході за часів активного будівництва залізниць. Бретт Макбейн купує шматок землі з єдиним в окрузі джерелом води, передбачаючи, що поїзди будуть ходити саме через цю ділянку. Чоловік стає мішенню для бандитів, які хочуть викупити землю. В результаті вони вбивають Бретта, зробивши молоду Джилл вдовою. Але і вона не погоджується продавати ферму ділку. Тоді він замовляє вбивство жінки, найнявши кращого стрільця на Дикому Заході. Але у красуні знайдуться захисники - відомий бандит Шайєнн і волоцюга на прізвисько «Гармоніка».",
                        "Злобный бизнесмен нанимает киллера, чтобы тот убил вдову, отказавшуюся продавать ему землю. Но женщину берётся защищать загадочный стрелок по прозвищу Гармоника.",
                        new File("./src/main/resources/img/Once-upon-a-time-in-the-west.jpg")
                )
        );

        return movieList;
    }*/


}
