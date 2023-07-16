package com.valentingiarra.notaapp.controllers;

import com.valentingiarra.notaapp.model.ApiResponse;
import com.valentingiarra.notaapp.persistence.entities.User;
import com.valentingiarra.notaapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ---- GET
    @GetMapping("")
    public ResponseEntity<ApiResponse> getAllUsers() {
        List<User> users = userService.getAllUsers();

        if (users.size() == 0) {
            return new ResponseEntity<>(new ApiResponse("empty", users), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse("success", users), HttpStatus.OK);
    }

    // ---- POST
    @PostMapping("")
    public ResponseEntity<ApiResponse> createUser(@RequestBody User data) {
        Object user = userService.createUser(data);

        if (user.getClass() != User.class) {
            return new ResponseEntity<>(new ApiResponse("error", user), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse("success", user), HttpStatus.CREATED);
    }

    // ---- PUT

    // ---- DELETE
}
