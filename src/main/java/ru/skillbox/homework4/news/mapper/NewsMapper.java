package ru.skillbox.homework4.news.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ru.skillbox.homework4.commentary.dto.CommentariesDto;
import ru.skillbox.homework4.commentary.model.Commentary;
import ru.skillbox.homework4.news.dto.NewsDto;
import ru.skillbox.homework4.news.dto.FullNewsDto;
import ru.skillbox.homework4.news.model.News;
import ru.skillbox.homework4.news.model.category.Category;
import ru.skillbox.homework4.user.model.User;
import java.util.List;
import java.util.stream.Collectors;

import static ru.skillbox.homework4.commentary.mapper.CommentaryMapper.COMMENTARY_MAPPER;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NewsMapper {

    NewsMapper NEWS_MAPPER = Mappers.getMapper(NewsMapper.class);

    NewsDto toNewsDto(News news);

    FullNewsDto toFullNewsDto(News news);

    default FullNewsDto setCommentariesList(FullNewsDto fullNewsDto, List<Commentary> commentariesList) {

        List<CommentariesDto> commentariesDtoList = commentariesList.stream()
                .map(COMMENTARY_MAPPER::setNewsAndAuthorsOfComments)
                .collect(Collectors.toList());

        fullNewsDto.setCommentaryList(commentariesDtoList);

        return fullNewsDto;
    }

    @Mapping(source = "category.id", target = "id")
    News toNews(NewsDto newsDto, User user, Category category);

    default News setCategoryToNewsAndUserAsOwner(NewsDto newsDto, User user, Category category) {

        News news = toNews(newsDto, user, category);
        news.setUser(user);
        news.setCategory(category);

        return news;
    }
}
