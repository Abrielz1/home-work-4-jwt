package ru.skillbox.homework4.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import java.util.List;
import ru.skillbox.homework4.user.dto.UserDto;
import ru.skillbox.homework4.user.model.User;
import ru.skillbox.homework4.user.service.UserServiceImpl;
import static ru.skillbox.homework4.user.mapper.UserMapper.USER_MAPPER;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserServiceImpl userService;

    private User user1;

    private UserDto user1Dto;

    @BeforeEach
    void beforeEach() {

        user1 = User.builder()
                .id(1L)
                .name("User1 name")
                .email("user1@mail.com")
                .build();

        user1Dto = USER_MAPPER.toUserDto(user1);
    }

    @Test
    void getAllUsersTest() throws Exception {

        when(userService.findAll(Pageable.ofSize(10)))
                .thenReturn(List.of(user1Dto));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(user1Dto))));
    }

    @Test
    void getByIdTest() throws Exception {

        when(userService.getById(anyLong()))
                .thenReturn(user1Dto);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(user1Dto)));
    }

    @Test
    void createUserTest() throws Exception {

        when(userService.create(any(UserDto.class)))
                .thenReturn(user1Dto);

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(user1Dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(user1Dto)));
    }

    @Test
    void updateUserByIdTest() throws Exception {

        when(userService.update(anyLong(), any(UserDto.class)))
                .thenReturn(user1Dto);

        mockMvc.perform(put("/users/1")
                        .content(mapper.writeValueAsString(user1Dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(user1Dto)));
    }

    @Test
    void deleteTest() throws Exception {

        when(userService.delete(anyLong()))
                .thenReturn(user1Dto);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }
}