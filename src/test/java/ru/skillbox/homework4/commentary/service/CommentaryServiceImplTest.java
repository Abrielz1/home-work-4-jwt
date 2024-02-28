package ru.skillbox.homework4.commentary.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageRequest;
import ru.skillbox.homework4.commentary.dto.CommentariesDto;
import ru.skillbox.homework4.commentary.model.Commentary;
import ru.skillbox.homework4.commentary.repository.CommentaryRepository;
import ru.skillbox.homework4.news.dto.NewsDto;
import ru.skillbox.homework4.news.model.News;
import ru.skillbox.homework4.news.model.category.Category;
import ru.skillbox.homework4.news.repository.CategoryRepository;
import ru.skillbox.homework4.news.repository.NewsRepository;
import ru.skillbox.homework4.user.dto.UserDto;
import ru.skillbox.homework4.user.model.User;
import ru.skillbox.homework4.user.repository.UserRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static ru.skillbox.homework4.commentary.mapper.CommentaryMapper.COMMENTARY_MAPPER;
import static ru.skillbox.homework4.news.mapper.NewsMapper.NEWS_MAPPER;
import static ru.skillbox.homework4.user.mapper.UserMapper.USER_MAPPER;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CommentaryServiceImplTest {

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CommentaryRepository commentaryRepository;

    @InjectMocks
    private CommentaryServiceImpl commentaryService;

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
                .user(user1)
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
                .user(user2)
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
    void findAllCommentary() {

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));

        when(categoryRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(category1));

        when(commentaryRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(commentary1));

        when(newsRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(news1));

        when(commentaryRepository.findAllByNewsId(anyLong(), any(PageRequest.class)))
                .thenReturn(List.of(commentary1, commentary2));

        commentary1.setUser(user1);
        commentary1.setNews(news1);
        commentary2.setUser(user1);
        commentary2.setNews(news1);
        commentary3.setUser(user2);
        commentary3.setNews(news1);

        PageRequest p = PageRequest.of(0, 20);
        List<CommentariesDto> list = commentaryService.findAllCommentary(user1.getId(), p);

        UserDto userDto1 = USER_MAPPER.toUserDto(user1);
        NewsDto newsDto1 = NEWS_MAPPER.toNewsDto(news1);

        assertThat(list.size()).isEqualTo(2);
        assertThat(list.get(0).getId()).isEqualTo(1L);
        assertThat(list.get(1).getId()).isEqualTo(2L);
        assertThat(list.get(0).getAuthor()).isEqualTo(userDto1);
        assertThat(list.get(0).getNews()).isEqualTo(newsDto1);
        assertThat(list.get(1).getNews()).isEqualTo(newsDto1);
        assertThat(list.get(0).getCommentaryText()).isEqualToIgnoringCase(commentary1.getCommentaryText());
        assertThat(list.get(1).getCommentaryText()).isEqualToIgnoringCase(commentary2.getCommentaryText());
    }

    @Test
    void findCommentaryById() {

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));

        when(categoryRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(category1));

        when(commentaryRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(commentary1));

        when(newsRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(news1));

        commentary1.setUser(user1);
        commentary1.setNews(news1);
        commentary2.setUser(user1);
        commentary2.setNews(news1);
        commentary3.setUser(user2);
        commentary3.setNews(news1);

        CommentariesDto commentariesDto = commentaryService.findCommentaryById(news1.getId(),
                user1.getId(),
                commentary1.getId());
        CommentariesDto commentariesDtoTest = COMMENTARY_MAPPER.setNewsAndAuthorsOfComments(commentary1);

        assertThat(commentariesDto).isEqualTo(commentariesDtoTest);
    }

    @Test
    void createCommentary() {

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));

        when(categoryRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(category1));

        when(commentaryRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(commentary1));

        when(newsRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(news1));

        commentary1.setUser(user1);
        commentary1.setNews(news1);
        commentary2.setUser(user1);
        commentary2.setNews(news1);
        commentary3.setUser(user2);
        commentary3.setNews(news1);

        CommentariesDto commentariesDtoTest = COMMENTARY_MAPPER.setNewsAndAuthorsOfComments(commentary1);
        CommentariesDto commentariesDto = commentaryService.createCommentary(
                news1.getId(),
                user1.getId(),
                commentariesDtoTest);

        commentariesDto.setId(1L);

        assertThat(commentariesDto).isEqualTo(commentariesDtoTest);
    }

    @Test
    void updateCommentaryById() {

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));

        when(categoryRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(category1));

        when(commentaryRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(commentary1));

        when(newsRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(news1));

        commentary1.setUser(user1);
        commentary1.setNews(news1);
        commentary2.setUser(user1);
        commentary2.setNews(news1);
        commentary3.setUser(user2);
        commentary3.setNews(news1);

        CommentariesDto commentariesDtoTest = COMMENTARY_MAPPER.setNewsAndAuthorsOfComments(commentary1);
        CommentariesDto commentariesDto = commentaryService.updateCommentaryById(
                news1.getId(),
                user1.getId(),
                commentariesDtoTest.getId(),
                commentariesDtoTest);

        commentariesDto.setId(1L);

        assertThat(commentariesDto).isEqualTo(commentariesDtoTest);
    }

    @Test
    void deleteCommentaryById() {

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));

        when(categoryRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(category1));

        when(commentaryRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(commentary1));

        when(newsRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(news1));

        commentary1.setUser(user1);
        commentary1.setNews(news1);
        commentary2.setUser(user1);
        commentary2.setNews(news1);
        commentary3.setUser(user2);
        commentary3.setNews(news1);

        CommentariesDto commentariesDto = commentaryService.deleteCommentaryById(news1.getId(), commentary1.getId(), user1.getId());
        CommentariesDto commentariesDtoTest = COMMENTARY_MAPPER.setNewsAndAuthorsOfComments(commentary1);

        assertThat(commentariesDto).isEqualTo(commentariesDtoTest);
    }
}