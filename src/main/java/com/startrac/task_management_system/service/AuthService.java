package com.startrac.task_management_system.service;

import com.startrac.task_management_system.dto.UserInputDTO;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<?> createUser(UserInputDTO userInputDTO);

    ResponseEntity<?> loginUser(UserInputDTO userInputDTO);

}
