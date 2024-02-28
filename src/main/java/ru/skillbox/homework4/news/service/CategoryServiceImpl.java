package ru.skillbox.homework4.news.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.homework4.exception.exceptions.ObjectNotFoundException;
import ru.skillbox.homework4.news.dto.category.CategoryDto;
import ru.skillbox.homework4.news.dto.category.CategoryNewDto;
import ru.skillbox.homework4.news.model.category.Category;
import ru.skillbox.homework4.news.repository.CategoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import static ru.skillbox.homework4.news.mapper.CategoryMapper.CATEGORY_MAPPER;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    @Override
    public List<CategoryDto> findAll() {
        return repository.findAll()
                .stream()
                .map(CATEGORY_MAPPER::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto findCategoryById(Long categoryId) {

        return CATEGORY_MAPPER.toCategoryDto(checkCategoryById(categoryId));
    }

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryNewDto newDto) {

        Category category = CATEGORY_MAPPER.toCategory(newDto);
        repository.save(category);

        log.info("Category was created");
        return CATEGORY_MAPPER.toCategoryDto(category);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long categoryId, CategoryDto categoryDto) {

        Category categoryDb = checkCategoryById(categoryId);

        if (categoryDb.getName() != null) {
            categoryDb.setName(categoryDto.getName());
        }

        repository.save(categoryDb);
        log.info("Category was updated");

        return CATEGORY_MAPPER.toCategoryDto(categoryDb);
    }

    @Override
    @Transactional
    public void deleteCategoryById(Long categoryId) {

        repository.delete(checkCategoryById(categoryId));
    }

    private Category checkCategoryById(Long categoryId) {

        return repository.findById(categoryId).orElseThrow(() -> {

            log.warn("Category with id {} was not found", categoryId);
            throw new ObjectNotFoundException("Category was not found");
        });
    }
}
