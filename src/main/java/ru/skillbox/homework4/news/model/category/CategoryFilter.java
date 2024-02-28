package ru.skillbox.homework4.news.model.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryFilter {

    private String categoryName;

    private String newsName;

    private Long newsOwnerId;
}
