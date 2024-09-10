package com.startrac.task_management_system.service;

import com.startrac.task_management_system.model.UserData;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {

    String generateToken(UserData user);

    boolean validateToken(String jwsToken, UserDetails userDetails);

    String extractUserName(String token);
}
