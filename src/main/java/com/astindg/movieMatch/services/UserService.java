package com.astindg.movieMatch.services;

import com.astindg.movieMatch.model.Movie;
import com.astindg.movieMatch.model.User;
import com.astindg.movieMatch.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.MINUTES;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final LinkedList<LocalDateTime> messageCounter;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.messageCounter = new LinkedList<>();
    }

    public void save(User user){
        this.userRepository.save(user);
    }

    public Optional<User> findByChatId(Long chatId){
        return Optional.ofNullable(this.userRepository.findByChatId(chatId));
    }

    public Integer getUsersAmount(){
        return userRepository.getUsersAmount();
    }

    public void saveFriendByIds(Integer userId, Integer friendId){
        this.userRepository.saveFriendByIds(userId, friendId);
    }

    public void saveLikedMovie(User user, Movie movie){
        this.userRepository.saveFavoriteMovie(user.getId(), movie.getId());
    }

    public void saveDislikedMovie(User user, Movie movie){
        this.userRepository.saveDislikedMovie(user.getId(), movie.getId());
    }

    public List<User> findAll(){
        return this.userRepository.findAll();
    }

    public void incrementMessageCounter(){
        this.messageCounter.add(LocalDateTime.now());
    }

    public Integer getMessageCount(Integer minutes){
        LocalDateTime now = LocalDateTime.now();

        if(this.messageCounter.isEmpty() || MINUTES.between(this.messageCounter.get(0), now) <= minutes){
            return this.messageCounter.size();
        }

        Integer count = 0;

        ListIterator<LocalDateTime> iterator = this.messageCounter.listIterator(this.messageCounter.size()-1);
        LocalDateTime dateTime = iterator.previous();
        while (iterator.hasPrevious()){
            if(MINUTES.between(dateTime, now) <= minutes){
                count++;
            } else {
                break;
            }
            dateTime = iterator.previous();
        }

        return count;
    }

    public Optional<User> findById(Integer id) {
        return this.userRepository.findById(id);
    }

    public void deleteFavoriteMovies(User user, Movie movie) {
        user.getFavoriteMovies().remove(movie);
        this.userRepository.deleteFavoriteMovie(user.getId(), movie.getId());
    }

    public void deleteDislikedMovie(User user, Movie movie) {
        user.getDislikedMovies().remove(movie);
        this.userRepository.deleteDislikedMovie(user.getId(), movie.getId());
    }

    public void saveLanguage(User user) {
        this.userRepository.saveLanguage(user.getId(), user.getLanguage().toString());
    }

    public void deleteFriend(User user, User friend) {
        this.userRepository.deleteFriend(user.getId(), friend.getId());
    }
}
