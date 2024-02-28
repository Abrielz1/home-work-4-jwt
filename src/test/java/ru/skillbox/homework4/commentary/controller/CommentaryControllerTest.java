package ru.skillbox.homework4.commentary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.skillbox.homework4.commentary.dto.CommentariesDto;
import ru.skillbox.homework4.commentary.model.Commentary;
import ru.skillbox.homework4.commentary.repository.CommentaryRepository;
import ru.skillbox.homework4.commentary.service.CommentaryServiceImpl;
import ru.skillbox.homework4.news.dto.FullNewsDto;
import ru.skillbox.homework4.news.dto.NewsDto;
import ru.skillbox.homework4.news.dto.category.CategoryDto;
import ru.skillbox.homework4.news.dto.category.CategoryNewDto;
import ru.skillbox.homework4.news.model.News;
import ru.skillbox.homework4.news.model.category.Category;
import ru.skillbox.homework4.news.repository.NewsRepository;
import ru.skillbox.homework4.user.dto.UserDto;
import ru.skillbox.homework4.user.model.User;
import ru.skillbox.homework4.user.repository.UserRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.skillbox.homework4.commentary.mapper.CommentaryMapper.COMMENTARY_MAPPER;
import static ru.skillbox.homework4.news.mapper.CategoryMapper.CATEGORY_MAPPER;
import static ru.skillbox.homework4.news.mapper.NewsMapper.NEWS_MAPPER;
import static ru.skillbox.homework4.user.mapper.UserMapper.USER_MAPPER;

@WebMvcTest(CommentaryController.class)
@AutoConfigureMockMvc
class CommentaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CommentaryServiceImpl service;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private NewsRepository newsRepository;

    @MockBean
    private CommentaryRepository commentaryRepository;

    private User user1;

    private UserDto user1Dto;

    private User user2;

    private UserDto user2Dto;

    private CategoryDto categoryDto1;

    private CategoryDto categoryDto2;

    Category category1;

    Category category2;

    private CategoryNewDto categoryNewDto1;

    private CategoryNewDto categoryNewDto2;

    private Commentary commentary1;

    private Commentary commentary2;

    private Commentary commentary3;

    private CommentariesDto commentariesDto1;

    private CommentariesDto commentariesDto2;

    private CommentariesDto commentariesDto3;

    private News news1;

    private NewsDto newsDto1;

    private FullNewsDto fullNewsDto1;

    private NewsDto newsDto2;

    private NewsDto newsDto3;

    private News news2;

    private News news3;

    @BeforeEach
    void beforeEach() {

        user1 = User.builder()
                .id(1L)
                .name("User1 name")
                .email("user1@mail.com")
                .build();

        user1Dto = USER_MAPPER.toUserDto(user1);

        user2 = User.builder()
                .id(2L)
                .name("User2 name")
                .email("user2@mail.com")
                .build();

        user2Dto = USER_MAPPER.toUserDto(user2);

        categoryNewDto1 = CategoryNewDto.builder()
                .name("Celebrity")
                .build();

        categoryDto1 = CategoryDto.builder()
                .id(1L)
                .name("Celebrity")
                .build();

        categoryDto1 = CATEGORY_MAPPER.toCategoryDto(category1);

        categoryNewDto2 = CategoryNewDto.builder()
                .name("Sport")
                .build();

        categoryDto2 = CategoryDto.builder()
                .id(2L)
                .name("Sport")
                .build();

        Category category1 = CATEGORY_MAPPER.toCategory(categoryNewDto1);
        category1.setId(1L);

        news1 = News.builder()
                .id(1L)
                .newsName("Test name news 1")
                .newsMessage("Test message news 1")
                .category(category1)
                .user(user1)
                .build();

        newsDto1 = NEWS_MAPPER.toNewsDto(news1);

        fullNewsDto1 = NEWS_MAPPER.toFullNewsDto(news1);

        Category category2 = CATEGORY_MAPPER.toCategory(categoryNewDto2);
        category2.setId(2L);

        news2 = News.builder()
                .id(2L)
                .newsName("Test name news 2")
                .newsMessage("Test message news 2")
                .category(category2)
                .user(user1)
                .build();

        newsDto2 = NEWS_MAPPER.toNewsDto(news2);


        news3 = News.builder()
                .id(3L)
                .newsName("Test name news 3")
                .newsMessage("Test message news 3")
                .category(category1)
                .commentaryList(Collections.emptyList())
                .user(user2)
                .build();

        newsDto3 = NEWS_MAPPER.toNewsDto(news3);

        commentary1 = Commentary.builder()
                .id(1L)
                .user(user1)
                .news(news3)
                .commentaryText("Text 1")
                .build();

        commentary2 = Commentary.builder()
                .id(2L)
                .user(user2)
                .news(news3)
                .commentaryText("Text 2")
                .build();

        commentary3 = Commentary.builder()
                .id(3L)
                .user(user1)
                .news(news3)
                .commentaryText("Text 1-2")
                .build();

        news3.setCommentaryList(List.of(commentary1, commentary2, commentary3));
    }

    @Test
    void findAll() throws Exception {

        commentariesDto1 = COMMENTARY_MAPPER.setNewsAndAuthorsOfComments(commentary1);
        commentariesDto2 = COMMENTARY_MAPPER.setNewsAndAuthorsOfComments(commentary2);
        commentariesDto3 = COMMENTARY_MAPPER.setNewsAndAuthorsOfComments(commentary3);

        when(service.findAllCommentary(anyLong(), any()))
                .thenReturn(List.of(commentariesDto1, commentariesDto2, commentariesDto3));

        mockMvc.perform(get("/news/3/commentaries"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(commentariesDto1,
                        commentariesDto2,
                        commentariesDto3))));
    }

    @Test
    void findCommentaryById() throws Exception {

        commentariesDto1 = COMMENTARY_MAPPER.setNewsAndAuthorsOfComments(commentary1);

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));

        when(newsRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(news3));

        when(service.findCommentaryById(anyLong(), anyLong(), anyLong()))
                .thenReturn(commentariesDto1);

        mockMvc.perform(get("/news/3/commentaries/1?userId=1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(commentariesDto1)));
    }

    @Test
    void createCommentary() throws Exception {

        commentariesDto1 = COMMENTARY_MAPPER.setNewsAndAuthorsOfComments(commentary1);

        when(service.createCommentary(anyLong(), anyLong(), any(CommentariesDto.class)))
                .thenReturn(commentariesDto1);

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));

        when(newsRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(news3));

        mockMvc.perform(post("/news/3/commentaries?userId=1")
                        .content(mapper.writeValueAsString(commentariesDto1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(commentariesDto1)));
    }

    @Test
    void updateCommentaryById() throws Exception {

        commentariesDto1 = COMMENTARY_MAPPER.setNewsAndAuthorsOfComments(commentary1);

        when(service.updateCommentaryById(anyLong(), anyLong(), anyLong(), any(CommentariesDto.class)))
                .thenReturn(commentariesDto1);

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));

        when(newsRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(news3));

        when(commentaryRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(commentary1));

        commentariesDto1 = COMMENTARY_MAPPER.setNewsAndAuthorsOfComments(commentary1);

        mockMvc.perform(put("/news/3/commentaries/1?userId=1")
                        .content(mapper.writeValueAsString(commentariesDto1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(commentariesDto1)));
    }

    @Test
    void deleteCommentaryById() throws Exception {

        commentariesDto1 = COMMENTARY_MAPPER.setNewsAndAuthorsOfComments(commentary1);

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));

        when(newsRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(news3));

        when(service.findCommentaryById(anyLong(), anyLong(), anyLong()))
                .thenReturn(commentariesDto1);

        mockMvc.perform(delete("/news/3/commentaries/1?userId=1"))
                .andExpect(status().isNoContent());
    }
}