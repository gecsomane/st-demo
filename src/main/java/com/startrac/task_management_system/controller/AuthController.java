package com.startrac.task_management_system.controller;

import com.startrac.task_management_system.dto.UserInputDTO;
import com.startrac.task_management_system.service.AuthService;
import com.startrac.task_management_system.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserInputDTO userInputDTO) {
        return authService.createUser(userInputDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserInputDTO userInputDTO) {
        return authService.loginUser(userInputDTO);
    }
}
