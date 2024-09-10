package com.startrac.task_management_system.repository;

import com.startrac.task_management_system.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Short> {

    @Query("SELECT t FROM Task t WHERE t.userData.id = :userId")
    List<Task> findByUserId(Short userId);
}
