package com.startrac.task_management_system.service;

import com.startrac.task_management_system.model.UserData;
import com.startrac.task_management_system.dto.UserInputDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService {

    UserData findByUsernameAndPassword(String username, String password);

    Optional<UserData> findByUsername(String username);

    UserDetailsService userDetailsService();
}
