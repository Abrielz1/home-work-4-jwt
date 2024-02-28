package ru.skillbox.homework4.commentary.service;

import org.springframework.data.domain.PageRequest;
import ru.skillbox.homework4.commentary.dto.CommentariesDto;
import java.util.List;

public interface CommentaryService {

    List<CommentariesDto> findAllCommentary(Long newsId, PageRequest page);

    CommentariesDto findCommentaryById(Long newsId, Long userId, Long commentaryId);

    CommentariesDto createCommentary(Long newsId, Long userId, CommentariesDto commentariesDto);

    CommentariesDto updateCommentaryById(Long newsId, Long commentaryId, Long userId, CommentariesDto commentariesDto);

    CommentariesDto deleteCommentaryById(Long newsId, Long commentaryId, Long userId);
}
