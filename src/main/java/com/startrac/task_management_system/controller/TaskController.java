package com.startrac.task_management_system.controller;

import com.startrac.task_management_system.dto.TaskDTO;
import com.startrac.task_management_system.dto.TaskFilterDTO;
import com.startrac.task_management_system.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskDTO> findAllForUser() {
        return taskService.findAllForUser();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody TaskDTO taskDTO) {
        return new ResponseEntity<>(taskService.create(taskDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Short id, @RequestBody TaskDTO taskDTO) {
        return new ResponseEntity<>(taskService.update(id, taskDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Short id) {
        taskService.delete(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }

    @PostMapping("/search")
    public Map<Integer, List<TaskDTO>> searchTasks(@RequestBody TaskFilterDTO taskFilterDTO) {
        return taskService.searchTasks(taskFilterDTO);
    }
}
