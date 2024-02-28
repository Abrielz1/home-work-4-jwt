package ru.skillbox.homework4.news.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.skillbox.homework4.commentary.model.Commentary;
import ru.skillbox.homework4.news.dto.FullNewsDto;
import ru.skillbox.homework4.news.dto.NewsDto;
import ru.skillbox.homework4.news.dto.category.CategoryDto;
import ru.skillbox.homework4.news.dto.category.CategoryNewDto;
import ru.skillbox.homework4.news.model.News;
import ru.skillbox.homework4.news.model.category.Category;
import ru.skillbox.homework4.news.service.NewsServiceImpl;
import ru.skillbox.homework4.user.dto.UserDto;
import ru.skillbox.homework4.user.model.User;
import ru.skillbox.homework4.user.repository.UserRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.skillbox.homework4.news.mapper.CategoryMapper.CATEGORY_MAPPER;
import static ru.skillbox.homework4.news.mapper.NewsMapper.NEWS_MAPPER;
import static ru.skillbox.homework4.user.mapper.UserMapper.USER_MAPPER;

@WebMvcTest(NewsController.class)
@AutoConfigureMockMvc
class NewsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private NewsServiceImpl newsService;

    @MockBean
    private UserRepository userRepository;

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

        news1 = News.builder()
                .id(1L)
                .newsName("Test name news 1")
                .newsMessage("Test message news 1")
                .category(CATEGORY_MAPPER.toCategory(categoryNewDto1))
                .user(user1)
                .build();

        newsDto1 = NEWS_MAPPER.toNewsDto(news1);

        fullNewsDto1 = NEWS_MAPPER.toFullNewsDto(news1);

        news2 = News.builder()
                .id(2L)
                .newsName("Test name news 2")
                .newsMessage("Test message news 2")
                .category(CATEGORY_MAPPER.toCategory(categoryNewDto2))
                .user(user1)
                .build();

        newsDto2 = NEWS_MAPPER.toNewsDto(news2);

        news3 = News.builder()
                .id(3L)
                .newsName("Test name news 3")
                .newsMessage("Test message news 3")
                .category(CATEGORY_MAPPER.toCategory(categoryNewDto1))
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
    void findAllCriteria() {
    }

    @Test
    void findAll() throws Exception {

        when(newsService.findAll(any()))
                .thenReturn(Collections.singletonList(newsDto1));

        mockMvc.perform(get("/news"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(newsDto1))));
    }

    @Test
    void findNewsById() throws Exception {

        when(newsService.findNewsById(anyLong()))
                .thenReturn(fullNewsDto1);

        mockMvc.perform(get("/news/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(fullNewsDto1)));
    }

    @Test
    void createNews() throws Exception {

        when(newsService.createNews(anyLong(), anyLong(), any(NewsDto.class)))
                .thenReturn(newsDto1);

        mockMvc.perform(post("/news?userId=1&categoryId=1")
                        .content(mapper.writeValueAsString(newsDto1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(newsDto1)));
    }

    @Test
    void updateNewsById() throws Exception {

        when(newsService.updateNewsById(anyLong(), anyLong(), anyLong(), any(NewsDto.class)))
                .thenReturn(newsDto1);

        mockMvc.perform(put("/news/1?userId=1&categoryId=1")
                        .content(mapper.writeValueAsString(newsDto1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(newsDto1)));
    }

    @Test
    void deleteNewsById() throws Exception {

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));

        when(newsService.deleteNewsById(anyLong(), anyLong()))
                .thenReturn(newsDto1);

        mockMvc.perform(delete("/news/1?userId=1"))
                .andExpect(status().isNoContent())
                .andExpect(content().json(mapper.writeValueAsString(newsDto1)));
    }
}