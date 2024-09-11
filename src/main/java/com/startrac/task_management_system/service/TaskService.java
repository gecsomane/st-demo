package com.startrac.task_management_system.service;

import com.startrac.task_management_system.dto.TaskDTO;
import com.startrac.task_management_system.dto.TaskFilterDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Service
public interface TaskService {

    List<TaskDTO> findAllForUser();

    Map<Integer, List<TaskDTO>> searchTasks(TaskFilterDTO taskFilterDTO);

    TaskDTO create(TaskDTO taskDTO);

    TaskDTO update(Short id, TaskDTO taskDTO) ;

    void delete(@PathVariable Short id);

}
