package com.valentingiarra.notaapp.service;

import com.valentingiarra.notaapp.persistence.entities.Group;
import com.valentingiarra.notaapp.persistence.entities.User;
import com.valentingiarra.notaapp.persistence.repositories.GroupRepository;
import com.valentingiarra.notaapp.persistence.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public AuthService(UserRepository userRepository, GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    public Object createUser(User data) {
        if (userRepository.findFirstByEmail(data.getEmail()) != null) {
            return "That email is already used.";
        }

        if (userRepository.findFirstByUsername(data.getUsername()) != null) {
            return "That username is already used.";
        }

        // Hash password
        String hashedPassword = passwordEncoder().encode(data.getPassword());
        data.setPassword(hashedPassword);

        data.setUserRole(1);
        User user = userRepository.save(data);

        Group defaultGroup = new Group();
        defaultGroup.setName("Without group");
        defaultGroup.setUser(user);
        defaultGroup.setPosition(1);
        groupRepository.save(defaultGroup);

        return user;
    }

    public Object loginUser(User data) {
        User user = userRepository.findFirstByEmail(data.getEmail());
        if (user == null) {
            return "User doesn't exists.";
        }

        String hashedPassword = user.getPassword();

        if (passwordEncoder().matches(data.getPassword(), hashedPassword)) {
            return "Incorrect password.";
        }

        user.setPassword(null);
        return user;
    }
}
