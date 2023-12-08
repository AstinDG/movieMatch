package com.astindg.movieMatch.services;

import com.astindg.movieMatch.model.User;
import com.astindg.movieMatch.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(User user){
        this.userRepository.save(user);
    }

    public User findByChatId(Long chatId){
        return this.userRepository.findByChatId(chatId);
    }

    public void saveFriendByIds(Integer userId, Integer friendId){
        this.userRepository.saveFriendByIds(userId, friendId);
    }
}
