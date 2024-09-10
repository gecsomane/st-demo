package com.startrac.task_management_system.service;

import com.startrac.task_management_system.dto.TaskDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public interface TaskService {

    List<TaskDTO> findAllForUser();

    TaskDTO create(TaskDTO taskDTO);

    TaskDTO update(Short id, TaskDTO taskDTO) ;

    void delete(@PathVariable Short id);

}
