package com.startrac.task_management_system.service;

import com.startrac.task_management_system.dto.UserInputDTO;
import com.startrac.task_management_system.enumerated.UserRole;
import com.startrac.task_management_system.model.UserData;
import com.startrac.task_management_system.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public ResponseEntity<?> createUser(UserInputDTO userInputDTO) {
        try {
            UserData userData = new UserData();
            userData.setUsername(userInputDTO.getUsername());
            userData.setPassword(passwordEncoder.encode(userInputDTO.getPassword()));
            userData.setRole(UserRole.USER);
            userRepository.save(userData);
            return new ResponseEntity<>("User created: " + userData.getUsername(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("User not created!", HttpStatus.CONFLICT);
        }
    }

    @Override
    public ResponseEntity<?> loginUser(UserInputDTO userInputDTO) {
        if (userInputDTO.getUsername() == null || userInputDTO.getPassword() == null) {
            return new ResponseEntity<>("Username or password is null", HttpStatus.BAD_REQUEST);
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userInputDTO.getUsername(), userInputDTO.getPassword())
            );

            UserData userData = (UserData) authentication.getPrincipal();
            return ResponseEntity.ok().body(jwtService.generateToken(userData));


        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
