package ru.skillbox.homework4.news.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.homework4.common.Create;
import ru.skillbox.homework4.common.Update;
import ru.skillbox.homework4.news.dto.FullNewsDto;
import ru.skillbox.homework4.news.dto.NewsDto;
import ru.skillbox.homework4.news.model.category.CategoryFilter;
import ru.skillbox.homework4.news.service.NewsService;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/find-by-criteria/")
    @ResponseStatus(HttpStatus.OK)
    public List<NewsDto> findAllCriteria(
            @ModelAttribute CategoryFilter filter,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size) {

        PageRequest page = PageRequest.of(from / size, size);

        return newsService.filteredByCriteria(filter, page);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<NewsDto> findAll(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                 @Positive @RequestParam(defaultValue = "10") Integer size) {

        PageRequest page = PageRequest.of(from / size, size);

        return newsService.findAll(page);
    }

    @GetMapping("/{newsId}")
    @ResponseStatus(HttpStatus.OK)
    public FullNewsDto findNewsById(@Positive @PathVariable Long newsId) {

        return newsService.findNewsById(newsId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewsDto createNews(@Positive @RequestParam(name = "userId") Long userId,
                              @Positive @RequestParam(name = "categoryId") Long categoryId,
                              @Validated(Create.class) @RequestBody NewsDto newsDto) {

        return newsService.createNews(userId, categoryId, newsDto);
    }

    @PutMapping("/{newsId}")
    @ResponseStatus(HttpStatus.OK)
    public NewsDto updateNewsById(@Positive @RequestParam(name = "userId") Long userId,
                                  @Positive @RequestParam(name = "categoryId") Long categoryId,
                                  @Positive @PathVariable(name = "newsId") Long newsId,
                                  @Validated(Update.class) @RequestBody NewsDto newsDto) {

        return newsService.updateNewsById(userId, categoryId, newsId, newsDto);
    }


    @DeleteMapping("/{newsId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public NewsDto deleteNewsById(@Positive @PathVariable(name = "newsId") Long newsId,
                                  @Positive @RequestParam(name = "userId") Long userId) {

        return newsService.deleteNewsById(newsId, userId);
    }
}
