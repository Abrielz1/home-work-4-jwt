package ru.skillbox.homework4.news.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.AfterEach;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.when;
import org.mockito.quality.Strictness;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import org.mockito.Mock;
import ru.skillbox.homework4.commentary.model.Commentary;
import ru.skillbox.homework4.commentary.repository.CommentaryRepository;
import ru.skillbox.homework4.exception.exceptions.ObjectNotFoundException;
import ru.skillbox.homework4.news.dto.FullNewsDto;
import ru.skillbox.homework4.news.dto.NewsDto;
import ru.skillbox.homework4.news.dto.category.CategoryDto;
import ru.skillbox.homework4.news.model.News;
import ru.skillbox.homework4.news.model.category.Category;
import ru.skillbox.homework4.news.repository.CategoryRepository;
import ru.skillbox.homework4.news.repository.NewsRepository;
import ru.skillbox.homework4.user.model.User;
import ru.skillbox.homework4.user.repository.UserRepository;

import java.util.List;

import static ru.skillbox.homework4.news.mapper.CategoryMapper.CATEGORY_MAPPER;
import static ru.skillbox.homework4.news.mapper.NewsMapper.NEWS_MAPPER;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class NewsServiceImplTest {

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CommentaryRepository commentaryRepository;

    @InjectMocks
    private NewsServiceImpl newsService;

    private User user1;

    private User user2;

    private Category category1;

    private Category category2;

    private Commentary commentary1;

    private Commentary commentary2;

    private Commentary commentary3;

    private News news1;

    private News news2;

    private News news3;

    @BeforeEach
    void beforeEach() {

        user1 = User.builder()
                .id(1L)
                .name("User1 name")
                .email("user1@mail.com")
                .build();

        userRepository.save(user1);

        user2 = User.builder()
                .id(2L)
                .name("User2 name")
                .email("user2@mail.com")
                .build();

        userRepository.save(user2);

        category1 = Category.builder()
                .id(1L)
                .name("Celebrity")
                .build();

        categoryRepository.save(category1);

        category2 = Category.builder()
                .id(2L)
                .name("Sport")
                .build();

        categoryRepository.save(category2);

        news1 = News.builder()
                .id(1L)
                .newsName("Test name news 1")
                .newsMessage("Test message news 1")
                .category(category1)
                .user(user1)
                .build();

        newsRepository.save(news1);

        news2 = News.builder()
                .id(2L)
                .newsName("Test name news 2")
                .newsMessage("Test message news 2")
                .category(category2)
                .user(user1)
                .build();

        newsRepository.save(news2);

        news3 = News.builder()
                .id(3L)
                .newsName("Test name news 3")
                .newsMessage("Test message news 3")
                .category(category1)
                .commentaryList(Collections.emptyList())
                .user(user2)
                .build();

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
        newsRepository.save(news3);
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
        categoryRepository.deleteAll();
        commentaryRepository.deleteAll();
        newsRepository.deleteAll();
    }

    @Test
    void filteredByCriteria() {
        //  fail("test");
        //я хз, как тестить критерию
    }

    @Test
    void findAll() {

        user1 = User.builder()
                .id(1L)
                .name("User1 name")
                .email("user1@mail.com")
                .build();

        userRepository.save(user1);

        category1 = Category.builder()
                .id(1L)
                .name("Celebrity")
                .build();

        categoryRepository.save(category1);

        news1 = News.builder()
                .id(1L)
                .newsName("Test name news 1")
                .newsMessage("Test message news 1")
                .category(category1)
                .user(user1)
                .build();

        commentary1 = Commentary.builder()
                .id(1L)
                .user(user1)
                .news(news3)
                .commentaryText("Text 1")
                .build();

        commentaryRepository.save(commentary1);

        commentary1.setUser(user1);
        news1.setCommentaryList(List.of(commentary1));
        news1.setUser(user1);
        news1.setCategory(category1);

        newsRepository.save(news1);

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));

        when(categoryRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(category1));

        when(commentaryRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(commentary1));

        when(newsRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(news1));

        PageRequest p = PageRequest.of(0, 20);

        List<News> list = new ArrayList<>();
        list.add(news1);

        when(newsRepository.findAll(any(PageRequest.class)))
                .thenReturn((new PageImpl<>(list)));

        List<NewsDto> newsDtoList = newsService.findAll(p);

        assertEquals(1, newsDtoList.size());
    }

    @Test
    void findNewsById() {

        List<Commentary> list = new ArrayList<>();
        list.add(commentary1);
        list.add(commentary2);
        list.add(commentary3);

        when(commentaryRepository.findAll())
                .thenReturn(list);

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user2));

        when(categoryRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(category1));

        when(newsRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(news3));

        when(commentaryRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(commentary1));

        when(commentaryRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(commentary2));

        when(commentaryRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(commentary3));

        news3.setCommentaryList(list);

        CategoryDto categoryDto = CATEGORY_MAPPER.toCategoryDto(category1);

        FullNewsDto newsDto = newsService.findNewsById(news3.getId());

        assertEquals(3L, newsDto.getId());
        assertEquals("Test name news 3", newsDto.getNewsName());
        assertEquals("Test message news 3", newsDto.getNewsMessage());
        assertEquals(categoryDto, newsDto.getCategory());
        assertEquals(1L, newsDto.getCategory().getId());
        assertEquals("Celebrity", newsDto.getCategory().getName());
        assertEquals(3, newsDto.getCommentaryList().size());
        assertEquals("Text 1", newsDto.getCommentaryList().get(0).getCommentaryText());
        assertEquals("Text 2", newsDto.getCommentaryList().get(1).getCommentaryText());
        assertEquals("Text 1-2", newsDto.getCommentaryList().get(2).getCommentaryText());
    }

    @Test
    void createNews() {

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));

        when(categoryRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(category1));

        when(newsRepository.save(any(News.class)))
                .thenReturn(news1);

        NewsDto newsNewDto = NEWS_MAPPER.toNewsDto(news1);
        NewsDto newsDto = newsService.createNews(user1.getId(), category1.getId(), newsNewDto);

        assertEquals(1L, newsDto.getId());
    }

    @Test
    void updateNewsByIdByAllFieldsExceptOwner() {

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));

        when(categoryRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(category1));

        when(categoryRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(category2));

        when(newsRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(news1));

        when(newsRepository.save(any(News.class)))
                .thenReturn(news1);

        news1.setId(1L);
        news1.setCategory(category2);
        news1.setNewsName("Name1");
        news1.setNewsMessage("Message1");

        NewsDto newsNewDto = NEWS_MAPPER.toNewsDto(news1);

        NewsDto newsDto = newsService.updateNewsById(user1.getId(), category2.getId(), newsNewDto.getId(), newsNewDto);

        assertEquals(1L, newsDto.getId());
        assertEquals(category2.getId(), newsDto.getCategory().getId());
        assertEquals("Name1", newsDto.getNewsName());
        assertEquals("Message1", newsDto.getNewsMessage());
    }

    @Test
    void deleteNewsById() {

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));

        when(newsRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(news1));

        when(categoryRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(category1));

        CategoryDto categoryDto = CATEGORY_MAPPER.toCategoryDto(category1);
        NewsDto newsDto = newsService.deleteNewsById(news1.getId(), user1.getId());

        assertEquals(1, newsDto.getId());
        assertEquals("Test name news 1", newsDto.getNewsName());
        assertEquals("Test message news 1", newsDto.getNewsMessage());
        assertEquals(categoryDto, newsDto.getCategory());
        assertEquals(user1, news1.getUser());
    }

    @Test
    void ifUserNotPresentExceptionAreThrownWhenNewsUpdateTest() {

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        when(newsRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(news1));

        when(categoryRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(category2));

        when(newsRepository.save(any(News.class)))
                .thenReturn(news1);

        CategoryDto categoryDto = CATEGORY_MAPPER.toCategoryDto(category2);

        NewsDto newsNewDto = NEWS_MAPPER.toNewsDto(news1);
        newsNewDto.setCategory(categoryDto);
        newsNewDto.setNewsName("Name1");
        newsNewDto.setNewsMessage("Message1");

        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,
                () -> newsService.updateNewsById(0L, 1L, 1L, newsNewDto));

        assertEquals(
                "User was not found",
                exception.getMessage());
    }

    @Test
    void ifNewsNotPresentExceptionAreThrownWhenNewsUpdateTest() {

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));

        when(newsRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        when(categoryRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(category2));

        when(newsRepository.save(any(News.class)))
                .thenReturn(news1);

        CategoryDto categoryDto = CATEGORY_MAPPER.toCategoryDto(category2);

        NewsDto newsNewDto = NEWS_MAPPER.toNewsDto(news1);
        newsNewDto.setCategory(categoryDto);
        newsNewDto.setNewsName("Name1");
        newsNewDto.setNewsMessage("Message1");

        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,
                () -> newsService.updateNewsById(1L, 1L, 1L, newsNewDto));

        assertEquals(
                "News was not found",
                exception.getMessage());
    }

    @Test
    void ifCategoryNotPresentExceptionAreThrownWhenNewsUpdateTest() {

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));

        when(newsRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(news1));

        when(categoryRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        when(newsRepository.save(any(News.class)))
                .thenReturn(news1);

        CategoryDto categoryDto = CATEGORY_MAPPER.toCategoryDto(category2);

        NewsDto newsNewDto = NEWS_MAPPER.toNewsDto(news1);
        newsNewDto.setCategory(categoryDto);
        newsNewDto.setNewsName("Name1");
        newsNewDto.setNewsMessage("Message1");

        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,
                () -> newsService.updateNewsById(1L, 0L, 1L, newsNewDto));

        assertEquals(
                "Category was not found",
                exception.getMessage());
    }

    @Test
    void ifNewsNotPresentExceptionAreThrownWhenDeleteNewsById() {

        when(newsRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,
                () -> newsService.deleteNewsById(1L, user1.getId()));

        assertEquals(
                "News was not found",
                exception.getMessage());
    }

}