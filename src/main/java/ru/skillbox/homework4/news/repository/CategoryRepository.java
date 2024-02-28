package ru.skillbox.homework4.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.homework4.news.model.category.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
