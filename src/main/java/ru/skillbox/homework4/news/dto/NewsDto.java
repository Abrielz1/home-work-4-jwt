package ru.skillbox.homework4.news.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.homework4.news.dto.category.CategoryDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsDto {

    @Positive
    private Long id;

    @NotBlank
    private String newsMessage;

    @NotBlank
    private String newsName;

    private CategoryDto category;

    private Integer numberOfCommentaries;
}
