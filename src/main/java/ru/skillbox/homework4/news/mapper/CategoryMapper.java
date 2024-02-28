package ru.skillbox.homework4.news.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.skillbox.homework4.news.dto.category.CategoryDto;
import ru.skillbox.homework4.news.dto.category.CategoryNewDto;
import ru.skillbox.homework4.news.model.category.Category;

@Mapper
public interface CategoryMapper {

    CategoryMapper CATEGORY_MAPPER = Mappers.getMapper(CategoryMapper.class);

    Category toCategory(CategoryNewDto categoryNewDto);

    CategoryDto toCategoryDto(Category category);
}

