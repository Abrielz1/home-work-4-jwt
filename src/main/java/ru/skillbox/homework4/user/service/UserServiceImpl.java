package ru.skillbox.homework4.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.homework4.exception.exceptions.ObjectNotFoundException;
import ru.skillbox.homework4.user.dto.UserDto;
import ru.skillbox.homework4.user.mapper.UserMapper;
import ru.skillbox.homework4.user.model.User;
import ru.skillbox.homework4.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> findAll(Pageable page) {

        log.info("All users were sent");

        return userRepository.findAll(page).getContent()
                .stream().map(UserMapper.USER_MAPPER::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getById(Long id) {

        return UserMapper.USER_MAPPER.toUserDto(userRepository.findById(id).orElseThrow(() -> {
            log.warn("User with id {} was not found", id);

            throw new ObjectNotFoundException("User not found");
        }));
    }


    @Override
    @Transactional
    public UserDto create(UserDto userDto) {

        User user = UserMapper.USER_MAPPER.toUser(userDto);

        userRepository.save(user);
        log.info("User was created");

        return UserMapper.USER_MAPPER.toUserDto(user);
    }

    @Override
    @Transactional
    public UserDto update(Long id, UserDto userDto) {

        User userBd = checkUserById(id);

        if (userDto.getEmail() != null) {
            userBd.setEmail(userDto.getEmail());
        }

        if (userDto.getName() != null) {
            userBd.setName(userDto.getName());
        }

        log.info("User updated");
        userRepository.save(userBd);

        return UserMapper.USER_MAPPER.toUserDto(userBd);
    }

    @Override
    @Transactional
    public UserDto delete(Long id) {

        User user = checkUserById(id);

        userRepository.delete(user);
        log.info("User with id {} was deleted", id);

        return UserMapper.USER_MAPPER.toUserDto(user);
    }

    private User checkUserById(Long userId) {

        return userRepository.findById(userId).orElseThrow(() -> {
            log.warn("User with id {} was not found", userId);

            throw new ObjectNotFoundException("Пользователь не найден");
        });
    }
}
