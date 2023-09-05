package com.valentingiarra.notaapp.controllers;

import com.valentingiarra.notaapp.model.ApiResponse;
import com.valentingiarra.notaapp.persistence.entities.User;
import com.valentingiarra.notaapp.service.LogService;
import com.valentingiarra.notaapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(allowedHeaders = {"*"})
@RequestMapping("auth")
public class AuthController {
    private final UserService userService;
    private final LogService logService;

    @Autowired
    public AuthController(UserService userService, LogService logService) {
        this.userService = userService;
        this.logService = logService;
    }

    // ---- POST
    @PostMapping("login")
    public ResponseEntity<ApiResponse> loginUser(@RequestBody User data) {
        logService.WriteToLog("/auth/login", "POST", data.toString());

        Object user = userService.loginUser(data);

        if (user.getClass() != User.class) {
            return new ResponseEntity<>(new ApiResponse("error", user), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse("success", user), HttpStatus.OK);
    }

    @PostMapping("signup")
    public ResponseEntity<ApiResponse> createUser(@RequestBody User data) {
        logService.WriteToLog("/auth/signup", "POST", data.toString());

        Object user = userService.createUser(data);

        if (user.getClass() != User.class) {
            return new ResponseEntity<>(new ApiResponse("error", user), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse("success", user), HttpStatus.CREATED);
    }
}
