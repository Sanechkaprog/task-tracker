package com.alexkrug.backend.mapper;

import com.alexkrug.backend.domain.User;
import com.alexkrug.backend.dto.UserDto;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface ServiceMapper {

    UserDto userToDto(User user);
}
