package com.startrac.task_management_system.repository;

import com.startrac.task_management_system.enumerated.TaskStatus;
import com.startrac.task_management_system.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Short> {

    @Query("SELECT t FROM Task t WHERE t.userData.id = :userId")
    List<Task> findByUserId(Short userId);

    @Query("SELECT DISTINCT t FROM Task t " +
            "WHERE LOWER(title) LIKE CONCAT('%', LOWER( :#{#title} ), '%' ) " +
            "AND (LOWER(description) LIKE CONCAT('%', LOWER( :#{#description} ), '%' )) " +
            "AND (COALESCE(:statusList, null) IS NULL OR t.status IN :statusList) " +
            "AND (COALESCE(:dueDateFrom, null) IS NULL OR t.dueDate >= :dueDateFrom) " +
            "AND (COALESCE(:dueDateTo, null) IS NULL OR t.dueDate <= :dueDateTo) "
    )
    List<Task> search(
            @Param("title") String title,
            @Param("description") String description,
            @Param("statusList") List<TaskStatus> statusList,
            @Param("dueDateFrom") LocalDate dueDateFrom,
            @Param("dueDateTo") LocalDate dueDateTo
    );
}
