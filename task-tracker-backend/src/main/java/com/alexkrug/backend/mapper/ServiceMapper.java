package com.alexkrug.backend.mapper;

import com.alexkrug.backend.domain.Task;
import com.alexkrug.backend.domain.User;
import com.alexkrug.backend.dto.response.TaskDto;
import com.alexkrug.backend.dto.request.UserDto;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ServiceMapper {

    UserDto userToDto(User user);

    TaskDto toTaskDto(Task task);

}
