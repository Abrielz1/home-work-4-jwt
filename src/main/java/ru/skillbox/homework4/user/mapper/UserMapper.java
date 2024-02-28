package ru.skillbox.homework4.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ru.skillbox.homework4.user.model.User;
import ru.skillbox.homework4.user.dto.UserDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);

    UserDto toUserDto(User user);

    User toUser(UserDto userDto);
}
