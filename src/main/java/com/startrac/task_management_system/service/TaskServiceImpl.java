package com.startrac.task_management_system.service;

import com.startrac.task_management_system.dto.TaskDTO;
import com.startrac.task_management_system.enumerated.TaskStatus;
import com.startrac.task_management_system.enumerated.UserRole;
import com.startrac.task_management_system.model.Task;
import com.startrac.task_management_system.model.UserData;
import com.startrac.task_management_system.repository.TaskRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    public TaskServiceImpl(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    @Override
    public List<TaskDTO> findAllForUser() {
        UserData userData = (UserData) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (UserRole.ADMIN.equals(userData.getRole())) {
            return taskRepository.findAll().stream().map(this::mapEntityToDTO).collect(Collectors.toList());
        } else {
            return taskRepository.findByUserId(userData.getId()).stream().map(this::mapEntityToDTO).collect(Collectors.toList());
        }
    }

    @Override
    public TaskDTO create(TaskDTO taskDTO) {
        Task taskToCreate = new Task();

        setOwnership(taskToCreate, taskDTO);

        taskToCreate.setTitle(taskDTO.getTitle());
        taskToCreate.setDescripton(taskDTO.getDescripton());
        taskToCreate.setDueDate(taskDTO.getDueDate());
        taskToCreate.setStatus(Objects.nonNull(taskDTO.getStatus()) ? taskDTO.getStatus() : TaskStatus.TODO);

        Task task = taskRepository.save(taskToCreate);
        taskDTO.setOwnerUsername(task.getUser().getUsername());
        taskDTO.setId(task.getId());
        return taskDTO;
    }

    @Override
    public TaskDTO update(Short id, TaskDTO taskDTO) {
        checkExistenceAndOwnership(id);

        Optional<Task> task = taskRepository.findById(id);
        if (task.isEmpty()) {
            throw new RuntimeException("Updateable task not found!");
        }
        Task taskToUpdate = task.get();
        setOwnership(taskToUpdate, taskDTO);
        taskToUpdate.setStatus(taskDTO.getStatus());
        taskToUpdate.setTitle(taskDTO.getTitle());
        taskToUpdate.setDescripton(taskDTO.getDescripton());
        taskToUpdate.setDueDate(taskDTO.getDueDate());
        return mapEntityToDTO(taskRepository.save(taskToUpdate));
    }

    @Override
    public void delete(@PathVariable Short id) {
        checkExistenceAndOwnership(id);
        taskRepository.deleteById(id);
    }

    private TaskDTO mapEntityToDTO(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(task.getId());
        taskDTO.setId(task.getId());
        taskDTO.setTitle(task.getTitle());
        taskDTO.setDescripton(task.getDescripton());
        taskDTO.setDueDate(task.getDueDate());
        taskDTO.setStatus(task.getStatus());
        taskDTO.setOwnerUsername(task.getUser().getUsername());
        return taskDTO;
    }

    private void checkExistenceAndOwnership(Short taskId) {
        Optional<Task> taskToChange = taskRepository.findById(taskId);
        UserData userData = (UserData) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (taskToChange.isEmpty()) {
            throw new RuntimeException("Task not found!");
        } else if (!UserRole.ADMIN.equals(userData.getRole()) && !taskToChange.get().getUser().getId().equals(userData.getId())) {
            throw new RuntimeException("No ownership for task!");
        }
    }


    private void setOwnership(Task task, TaskDTO taskDTO) {
        boolean isOwnershipInDTOChangeable = Objects.nonNull(taskDTO.getOwnerUsername()) && userService.findByUsername(taskDTO.getOwnerUsername()).isPresent();
        boolean isOwnershipInDTONotChangeable = Objects.nonNull(taskDTO.getOwnerUsername()) && userService.findByUsername(taskDTO.getOwnerUsername()).isEmpty();
        UserData loggedInUserData = (UserData) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean loggedInUserIsAdmin = UserRole.ADMIN.equals(loggedInUserData.getRole());
        boolean isTaskExists = Objects.nonNull(task.getId()); // create or update
        UserData userDataToSave = null;

        if (loggedInUserIsAdmin && isOwnershipInDTOChangeable) {
            userDataToSave = userService.findByUsername(taskDTO.getOwnerUsername()).get();
        } else if (!isTaskExists && (!loggedInUserIsAdmin || Objects.isNull(taskDTO.getOwnerUsername()))) {
            userDataToSave = loggedInUserData;
        } else if (isOwnershipInDTONotChangeable) {
            throw new RuntimeException("User for new task ownership not found!");
        }

        if (Objects.nonNull(userDataToSave)) {
            task.setUser(userDataToSave);
        }
    }
}
