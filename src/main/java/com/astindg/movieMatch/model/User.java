package com.astindg.movieMatch.model;

import lombok.*;
import org.apache.commons.lang3.builder.EqualsExclude;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

import static org.hibernate.annotations.CascadeType.*;
import static org.hibernate.annotations.CascadeType.DELETE;

@Data
@NoArgsConstructor
@ToString(exclude = {"friends", "favoriteMovies", "dislikedMovies"})
@Entity
@EqualsAndHashCode(exclude = {"language", "friends", "favoriteMovies", "dislikedMovies"})
@Table(name = "Users")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "chat_id")
    private Long chatId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "favorite_movies",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id")
    )
    @Cascade({SAVE_UPDATE, DELETE})
    private Set<Movie> favoriteMovies;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "disliked_movies",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id")
    )
    private Set<Movie> dislikedMovies;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend")
    )
    private List<User> friends;

    @Column(name = "lang")
    @Enumerated(EnumType.STRING)
    private Language language = Language.UA;
}
