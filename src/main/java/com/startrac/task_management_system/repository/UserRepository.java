package com.startrac.task_management_system.repository;

import com.startrac.task_management_system.model.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserData, Short> {

    UserData findByUsernameAndPassword(String username, String password);

    Optional<UserData> findByUsername(String username);
}
