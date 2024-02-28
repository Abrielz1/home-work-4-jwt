package ru.skillbox.homework4.news.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.skillbox.homework4.news.model.News;
import ru.skillbox.homework4.news.model.category.CategoryFilter;

public interface NewsSpecification {

    static Specification<News> byNewsNameAndOwnerIdFilter(CategoryFilter filter) {

        return Specification.where(byNewsName(filter.getNewsName()))
                .and(byNewsCategoryName(filter.getCategoryName()))
                .and(byNewsOwnerId(filter.getNewsOwnerId()));
    }

    static Specification<News> byNewsOwnerId(Long newsOwnerId) {

        return (root, query, cb) -> {

            if (newsOwnerId == null) {
                return null;
            }

            return cb.equal(root.get("user").get("id"), newsOwnerId);
        };
    }

    static Specification<News> byNewsCategoryName(String categoryName) {

        return (root, query, cb) -> {

            if (categoryName == null) {
                return null;
            }

            return cb.equal(root.get("category").get("name"), categoryName);
        };
    }

    static Specification<News> byNewsName(String newsName) {

        return (root, query, cb) -> {

            if (newsName == null) {
                return null;
            }

            return cb.equal(root.get("newsName"), newsName);
        };
    }
}
