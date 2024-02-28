package ru.skillbox.homework4.news.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.homework4.commentary.model.Commentary;
import ru.skillbox.homework4.exception.exceptions.ObjectNotFoundException;
import ru.skillbox.homework4.news.dto.NewsDto;
import ru.skillbox.homework4.news.dto.FullNewsDto;
import ru.skillbox.homework4.news.model.News;
import ru.skillbox.homework4.news.model.category.Category;
import ru.skillbox.homework4.news.model.category.CategoryFilter;
import ru.skillbox.homework4.news.repository.CategoryRepository;
import ru.skillbox.homework4.news.repository.NewsRepository;
import ru.skillbox.homework4.news.repository.NewsSpecification;
import ru.skillbox.homework4.user.model.User;
import ru.skillbox.homework4.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static ru.skillbox.homework4.news.mapper.CategoryMapper.CATEGORY_MAPPER;
import static ru.skillbox.homework4.news.mapper.NewsMapper.NEWS_MAPPER;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    @Override
    public List<NewsDto> filteredByCriteria(CategoryFilter filter, PageRequest page) {

        log.info("Request category were sent!");
        return newsRepository.findAll(NewsSpecification.byNewsNameAndOwnerIdFilter(filter), page).stream()
                .map(NEWS_MAPPER::toNewsDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<NewsDto> findAll(PageRequest page) {

        List<News> newsList = newsRepository.findAll(page).getContent();
        List<NewsDto> newsDtoList = new ArrayList<>();

        for (News news : newsList) {
            NewsDto newsDto = NEWS_MAPPER.toNewsDto(news);
            newsDto.setNumberOfCommentaries(news.getCommentaryList().size());
            newsDtoList.add(newsDto);
        }

        log.info("List of news were sent!");
        return newsDtoList;
    }

    @Override
    public FullNewsDto findNewsById(Long newsId) {

        News news = checkNewsById(newsId);
        FullNewsDto fullNewsDto = NEWS_MAPPER.toFullNewsDto(news);
        List<Commentary> commentariesList = news.getCommentaryList();
        fullNewsDto = NEWS_MAPPER.setCommentariesList(fullNewsDto, commentariesList);

        log.info("News with id: {} was sent", newsId);
        return fullNewsDto;
    }

    @Override
    @Transactional
    public NewsDto createNews(Long userId, Long categoryId, NewsDto newsDto) {

        User user = checkUserById(userId);
        Category category = checkCategoryById(categoryId);

        News news = new News();
        news = newsRepository.save(NEWS_MAPPER.setCategoryToNewsAndUserAsOwner(newsDto, user, category));

        log.info("News was created");
        return NEWS_MAPPER.toNewsDto(news);
    }

    @Override
    @Transactional
    public NewsDto updateNewsById(Long userId,
                                  Long categoryId,
                                  Long newsId,
                                  NewsDto newsDto) {

        checkUserById(userId);
        News newsBd = checkNewsById(newsId);
        Category category = checkCategoryById(categoryId);

        if (newsDto.getCategory() == null) {
            newsDto.setCategory(CATEGORY_MAPPER.toCategoryDto(category));
        }

        if (!categoryId.equals(newsBd.getCategory().getId())) {
            newsBd.setCategory(category);
            System.out.println("newsBd ->" + newsBd);
        }

        if (newsDto.getNewsName() != null) {
            newsBd.setNewsName(newsDto.getNewsName());
        }

        if (newsDto.getNewsMessage() != null) {
            newsBd.setNewsMessage(newsDto.getNewsMessage());
        }

        newsRepository.save(newsBd);

        log.info("News was updated");
        return NEWS_MAPPER.toNewsDto(newsBd);
    }

    @Override
    @Transactional
    public NewsDto deleteNewsById(Long newsId, Long userId) {

        News news = checkNewsById(newsId);

        newsRepository.deleteById(newsId);

        log.info("News with id: {} was deleted!", newsId);
        return NEWS_MAPPER.toNewsDto(news);
    }


    private User checkUserById(Long userId) {

        return userRepository.findById(userId).orElseThrow(() -> {

            log.warn("User with id {} was not found", userId);
            throw new ObjectNotFoundException("User was not found");
        });
    }

    private News checkNewsById(Long newsId) {

        return newsRepository.findById(newsId).orElseThrow(() -> {

            log.warn("News with id {} was not found", newsId);
            throw new ObjectNotFoundException("News was not found");
        });
    }

    private Category checkCategoryById(Long categoryId) {

        return categoryRepository.findById(categoryId).orElseThrow(() -> {

            log.warn("Category with id {} was not found", categoryId);
            throw new ObjectNotFoundException("Category was not found");
        });
    }
}
