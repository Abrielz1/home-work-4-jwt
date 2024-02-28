package ru.skillbox.homework4.user.service;

import org.springframework.data.domain.Pageable;
import ru.skillbox.homework4.user.dto.UserDto;
import java.util.List;

public interface UserService {

    List<UserDto> findAll(Pageable page);

    UserDto getById(Long id);

    UserDto create(UserDto userDto);

    UserDto update(Long id, UserDto userDto);

    UserDto delete(Long id);
}
