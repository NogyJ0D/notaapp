package com.valentingiarra.notaapp.controllers;

import com.valentingiarra.notaapp.model.ApiResponse;
import com.valentingiarra.notaapp.model.JwtUtil;
import com.valentingiarra.notaapp.persistence.entities.User;
import com.valentingiarra.notaapp.service.AuthService;
import com.valentingiarra.notaapp.service.LogService;
import com.valentingiarra.notaapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(allowedHeaders = {"*"})
@RequestMapping("auth")
public class AuthController {
    private final AuthService authService;
    private final LogService logService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    public AuthController(AuthService authService, LogService logService) {
        this.authService = authService;
        this.logService = logService;
    }

    // ---- POST
    @PostMapping("login")
    public ResponseEntity<ApiResponse> loginUser(@RequestBody User data) {
        logService.WriteToLog("/auth/login", "POST", data.toString());

        Object user = authService.loginUser(data);

        if (user.getClass() != User.class) {
            return new ResponseEntity<>(new ApiResponse("error", user), HttpStatus.UNAUTHORIZED);
        }

        String token = jwtUtil.generateAuthToken((User) user);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        return new ResponseEntity<>(new ApiResponse("success", user), headers, HttpStatus.OK);
    }

    @PostMapping("signup")
    public ResponseEntity<ApiResponse> createUser(@RequestBody User data) throws Exception {
        logService.WriteToLog("/auth/signup", "POST", data.toString());

        Object user = authService.createUser(data);

        if (user.getClass() != User.class) {
            return new ResponseEntity<>(new ApiResponse("error", user), HttpStatus.BAD_REQUEST);
        }

        User userData = (User) authService.loginUser((User) user);

        String token = jwtUtil.generateAuthToken((User) user);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        return new ResponseEntity<>(new ApiResponse("success", user), headers, HttpStatus.CREATED);
    }
}
