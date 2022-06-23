package com.hcmus.lovelybackend.service.impl;

import com.hcmus.lovelybackend.entity.dao.UserDAO;
import com.hcmus.lovelybackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDAO userDAO = userRepository.findByEmail(username);
        if (userDAO != null) {
            List<SimpleGrantedAuthority> roles = Arrays.asList(new SimpleGrantedAuthority(userDAO.getRole().toString()));
            return new User(userDAO.getEmail(), userDAO.getPassword(), roles);
        }
        throw new UsernameNotFoundException("User not found with the name " + username);
    }

    public UserDetails getCurrentUserDetails(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (UserDetails) principal;
    }

//    public UserDAO save(LoginRequest user) {
//        UserDAO newUser = new UserDAO();
//        newUser.setEmail(user.getEmail());
//        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
//        return userRepository.save(newUser);
//    }

}