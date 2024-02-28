package ru.skillbox.homework4.commentary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.homework4.news.dto.NewsDto;
import ru.skillbox.homework4.user.dto.UserDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentariesDto {

    private Long id;

    @NotNull
    @NotBlank
    private String commentaryText;

    private NewsDto news;

    private UserDto author;
}

