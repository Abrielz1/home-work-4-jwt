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
import ru.skillbox.homework4.news.dto.category.CategoryDto;
import ru.skillbox.homework4.news.dto.category.CategoryNewDto;
import ru.skillbox.homework4.news.model.category.Category;
import ru.skillbox.homework4.news.service.CategoryServiceImpl;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.skillbox.homework4.news.mapper.CategoryMapper.CATEGORY_MAPPER;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CategoryServiceImpl categoryService;

    private CategoryNewDto categoryNewDto;

    private Category category;

    private CategoryDto categoryDto;

    @BeforeEach
    void beforeEach() {

        categoryNewDto = CategoryNewDto.builder()
                .name("IT")
                .build();

        category = CATEGORY_MAPPER.toCategory(categoryNewDto);

        category.setId(1L);

        categoryDto = CATEGORY_MAPPER.toCategoryDto(category);
    }

    @Test
    void findAll() throws Exception {

        when(categoryService.findAll())
                .thenReturn(List.of(categoryDto));

        mockMvc.perform(get("/news/category"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(categoryDto))));
    }

    @Test
    void findCategoryById() throws Exception {

        when(categoryService.findCategoryById(anyLong()))
                .thenReturn(categoryDto);

        mockMvc.perform(get("/news/category/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(categoryDto)));
    }

    @Test
    void createCategory() throws Exception {

        when(categoryService.createCategory(any(CategoryNewDto.class)))
                .thenReturn(categoryDto);

        mockMvc.perform(post("/news/category")
                        .content(mapper.writeValueAsString(categoryDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(categoryDto)));
    }

    @Test
    void updateCategory() throws Exception {

        when(categoryService.updateCategory(anyLong(), any(CategoryDto.class)))
                .thenReturn(categoryDto);

        mockMvc.perform(put("/news/category/1")
                        .content(mapper.writeValueAsString(categoryDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(categoryDto)));
    }

    @Test
    void deleteCategoryById() throws Exception {

        mockMvc.perform(delete("/news/category/1"))
                .andExpect(status().isNoContent());
    }
}