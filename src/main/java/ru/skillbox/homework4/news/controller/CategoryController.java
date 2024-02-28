package ru.skillbox.homework4.news.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.homework4.common.Create;
import ru.skillbox.homework4.common.Update;
import ru.skillbox.homework4.news.dto.category.CategoryDto;
import ru.skillbox.homework4.news.dto.category.CategoryNewDto;
import ru.skillbox.homework4.news.service.CategoryService;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/news/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> findAll() {

        return service.findAll();
    }

    @GetMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto findCategoryById(@PathVariable Long categoryId) {

        return service.findCategoryById(categoryId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Validated(Create.class) @RequestBody CategoryNewDto newDto) {

        return service.createCategory(newDto);
    }

    @PutMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto updateCategory(@PathVariable Long categoryId,
                                      @Validated(Update.class) @RequestBody CategoryDto categoryDto) {

        return service.updateCategory(categoryId, categoryDto);
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryById(@PathVariable Long categoryId) {

        service.deleteCategoryById(categoryId);
    }
}
