package ru.skillbox.homework4.news.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.homework4.commentary.dto.CommentariesDto;
import ru.skillbox.homework4.news.dto.category.CategoryDto;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FullNewsDto {

    @Positive
    private Long id;

    @NotBlank
    private String newsMessage;

    @NotBlank
    private String newsName;

    private CategoryDto category;

    private List<CommentariesDto> commentaryList;
}

