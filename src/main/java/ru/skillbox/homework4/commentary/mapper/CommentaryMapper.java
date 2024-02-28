package ru.skillbox.homework4.commentary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ru.skillbox.homework4.commentary.dto.CommentariesDto;
import ru.skillbox.homework4.commentary.model.Commentary;
import ru.skillbox.homework4.news.dto.NewsDto;
import ru.skillbox.homework4.news.model.News;
import ru.skillbox.homework4.user.dto.UserDto;
import ru.skillbox.homework4.user.model.User;
import static ru.skillbox.homework4.news.mapper.NewsMapper.NEWS_MAPPER;
import static ru.skillbox.homework4.user.mapper.UserMapper.USER_MAPPER;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentaryMapper {

    CommentaryMapper COMMENTARY_MAPPER = Mappers.getMapper(CommentaryMapper.class);

    @Mapping(target = "id", ignore = true)
    Commentary CommentariesDto(CommentariesDto commentariesDto, User user, News news);

    default CommentariesDto setAuthorIdAndNewsId(CommentariesDto commentariesDto, User user, News news) {

        commentariesDto.setAuthor(USER_MAPPER.toUserDto(user));
        commentariesDto.setNews(NEWS_MAPPER.toNewsDto(news));

        return commentariesDto;
    }

    CommentariesDto CommentaryToCommentariesDto(Commentary commentary);

    default CommentariesDto setNewsAndAuthorsOfComments(Commentary commentary) {

        UserDto userDto = USER_MAPPER.toUserDto(commentary.getUser());
        NewsDto newsDto = NEWS_MAPPER.toNewsDto(commentary.getNews());

        CommentariesDto commentariesDto = COMMENTARY_MAPPER.CommentaryToCommentariesDto(commentary);
        commentariesDto.setNews(newsDto);
        commentariesDto.setAuthor(userDto);

        return commentariesDto;
    }
}
