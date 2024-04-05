package com.astindg.movieMatch.services;

import com.astindg.movieMatch.model.Admin;
import com.astindg.movieMatch.repositories.AdminRepository;
import com.astindg.movieMatch.util.AdminDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService implements UserDetailsService {
    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Admin> foundAdmin =adminRepository.findByUsername(username);

        if(foundAdmin.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }

        return new AdminDetails(foundAdmin.get());
    }
}
