package com.alexkrug.backend.controller;

import com.alexkrug.backend.domain.User;
import com.alexkrug.backend.dto.TaskCreationDto;
import com.alexkrug.backend.dto.TaskDto;
import com.alexkrug.backend.dto.TaskEditDto;
import com.alexkrug.backend.security.UserSecurity;
import com.alexkrug.backend.service.TaskService;
import com.alexkrug.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private static final Logger log = LoggerFactory.getLogger(TaskController.class);
    private final TaskService taskService;
    private final UserService userService;
    @GetMapping
    public ResponseEntity<List<TaskDto>> getTasks(@Valid @AuthenticationPrincipal UserSecurity userSecurity) {
        String username = userSecurity.getUsername();
        return ResponseEntity
                .ok()
                .body(userService.getUserTasks(username));
    }

    @PostMapping()
    public ResponseEntity<TaskDto> saveTask(@Valid @AuthenticationPrincipal UserSecurity userSecurity, @Valid @RequestBody TaskCreationDto taskCreation) {
        String username = userSecurity.getUsername();
        User user = userService.getUser(username);
        TaskDto taskDto = taskService.saveTask(user, taskCreation);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(taskDto);

    }

    @PatchMapping("{id}")
    public ResponseEntity<TaskDto> editTask(
            @PathVariable("id") Long taskId,
            @Valid @AuthenticationPrincipal UserSecurity userSecurity,
            @Valid @RequestBody TaskEditDto taskEditDto
            ) {
        String username = userSecurity.getUsername();
        TaskDto taskDto = taskService.editTask(taskId, username, taskEditDto);
        return ResponseEntity
                .ok()
                .body(taskDto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteTask(
            @PathVariable("id") Long taskId,
            @Valid @AuthenticationPrincipal UserSecurity userSecurity
    ) {
        String username = userSecurity.getUsername();
        taskService.deleteTask(taskId, username);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();

    }
}
