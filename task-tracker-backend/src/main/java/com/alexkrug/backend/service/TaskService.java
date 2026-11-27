package com.alexkrug.backend.service;

import com.alexkrug.backend.domain.Task;
import com.alexkrug.backend.domain.User;
import com.alexkrug.backend.dto.TaskCreationDto;
import com.alexkrug.backend.dto.TaskDto;
import com.alexkrug.backend.dto.TaskEditDto;
import com.alexkrug.backend.exception.TaskNotExistException;
import com.alexkrug.backend.mapper.ServiceMapper;
import com.alexkrug.backend.repository.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;
    private final ServiceMapper serviceMapper;

    public TaskDto saveTask(User user, TaskCreationDto taskCreationDto) {
        Task task = new Task(
                taskCreationDto.title(),
                taskCreationDto.description(),
                LocalDateTime.now(),
                false,
                null,
                user
        );
        taskRepository.save(task);
        log.info("TASK SAVED");
        return serviceMapper.toTaskDto(task);
    }

    public TaskDto editTask(Long taskId, String username, TaskEditDto taskEditDto) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        Task task = checkedTask(optionalTask, username, taskId);
        task.setDescription(taskEditDto.description());
        task.setTitle(taskEditDto.title());
        if (taskEditDto.completed()) {
            task.setCompleted(true);
            task.setCompletedAt(LocalDateTime.now());
        } else {
            task.setCompleted(false);
            task.setCompletedAt(null);
        }
        return serviceMapper.toTaskDto(task);
    }

    public void deleteTask(Long taskId, String username) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        Task task = checkedTask(optionalTask, username, taskId);
        taskRepository.delete(task);
    }

    private Task checkedTask(Optional<Task> optionalTask, String username, Long taskId) {
        if (optionalTask.isEmpty()) {
            throw new TaskNotExistException("Task with %d id not found".formatted(taskId));
        }
        Task task = optionalTask.get();
        String taskBelongTo = task.getUser().getUsername();
        if (!taskBelongTo.equals(username)) {
            throw new TaskNotExistException("Task with %d id not found".formatted(taskId));
        }
        return task;
    }


}
