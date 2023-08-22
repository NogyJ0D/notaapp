package com.valentingiarra.notaapp.service;

import com.valentingiarra.notaapp.persistence.entities.Group;
import com.valentingiarra.notaapp.persistence.entities.User;
import com.valentingiarra.notaapp.persistence.repositories.GroupRepository;
import com.valentingiarra.notaapp.persistence.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    @Autowired
    public UserService(UserRepository userRepository, GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Object createUser(User data) {
        if (userRepository.findFirstByEmail(data.getEmail()) != null) {
            return "That email is already used.";
        }

        if (userRepository.findFirstByUsername(data.getUsername()) != null) {
            return "That username is already used.";
        }

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

        if (!Objects.equals(user.getPassword(), data.getPassword())) {
            return "Incorrect password.";
        }

        user.setPassword("");
        return user;
    }
}
