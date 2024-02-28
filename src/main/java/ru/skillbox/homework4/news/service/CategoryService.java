package ru.skillbox.homework4.news.service;

import ru.skillbox.homework4.news.dto.category.CategoryDto;
import ru.skillbox.homework4.news.dto.category.CategoryNewDto;
import java.util.List;

public interface CategoryService {

    List<CategoryDto> findAll();

    CategoryDto findCategoryById(Long categoryId);

    CategoryDto createCategory(CategoryNewDto newDto);

    CategoryDto updateCategory(Long categoryId, CategoryDto categoryDto);

    void deleteCategoryById(Long categoryId);
}
