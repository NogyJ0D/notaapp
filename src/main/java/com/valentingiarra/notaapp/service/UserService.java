package com.valentingiarra.notaapp.service;

import com.valentingiarra.notaapp.persistence.entities.User;
import com.valentingiarra.notaapp.persistence.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public int getUserRoleById(Long id) {return userRepository.findUserRoleById(id);}
}
