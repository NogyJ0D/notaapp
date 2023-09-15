package com.valentingiarra.notaapp.controllers;

import com.valentingiarra.notaapp.model.ApiResponse;
import com.valentingiarra.notaapp.model.JwtUtil;
import com.valentingiarra.notaapp.persistence.entities.User;
import com.valentingiarra.notaapp.service.LogService;
import com.valentingiarra.notaapp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin(allowedHeaders = {"*"})
@RequestMapping("users")
public class UserController {
    private final UserService userService;
    private final LogService logService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, LogService logService) {
        this.userService = userService;
        this.logService = logService;
    }

    // ---- GET
    @GetMapping("")
    public ResponseEntity<ApiResponse> getAllUsers(HttpServletRequest req) {
        ////////AUTH/////////
        String access = jwtUtil.canAccess(req, 100);
        if (!Objects.equals(access, "success")) {
            return new ResponseEntity<>(new ApiResponse("error", access), HttpStatus.UNAUTHORIZED);
        }
        /////////////////////

        logService.WriteToLog("/users", "GET", null);

        List<User> users = userService.getAllUsers();

        if (users.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("empty", users), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse("success", users), HttpStatus.OK);
    }

    // ---- POST

    // ---- PUT

    // ---- DELETE
}
