package com.startrac.task_management_system.service;

import com.startrac.task_management_system.model.UserData;
import com.startrac.task_management_system.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserData findByUsernameAndPassword(String username, String password) {
        UserData userData = userRepository.findByUsernameAndPassword(username, password);
        if(userData == null){
            throw new UsernameNotFoundException("User not found!");
        }
        return userData;
        // lol
    }

    @Override
    public Optional<UserData> findByUsername(String username) {
        Optional<UserData> userData = userRepository.findByUsername(username);
        if(userData.isEmpty()){
            throw new UsernameNotFoundException("User not found!");
        }
        return userData;
    }

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }
}
