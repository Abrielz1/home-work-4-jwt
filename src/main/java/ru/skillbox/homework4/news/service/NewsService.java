package ru.skillbox.homework4.news.service;

import org.springframework.data.domain.PageRequest;
import ru.skillbox.homework4.news.dto.FullNewsDto;
import ru.skillbox.homework4.news.dto.NewsDto;
import ru.skillbox.homework4.news.model.category.CategoryFilter;
import java.util.List;

public interface NewsService {

    List<NewsDto> filteredByCriteria(CategoryFilter filter, PageRequest page);

    List<NewsDto> findAll(PageRequest page);

    FullNewsDto findNewsById(Long id);

    NewsDto deleteNewsById(Long id, Long userId);

    NewsDto createNews(Long id, Long categoryId, NewsDto newsDto);

    NewsDto updateNewsById(Long userId, Long categoryId, Long newsId, NewsDto newsDto);
}
