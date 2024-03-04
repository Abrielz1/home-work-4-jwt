package ru.skillbox.homework4.commentary.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skillbox.homework4.commentary.dto.CommentariesDto;
import java.util.List;

public interface CommentaryService {

    List<CommentariesDto> findAllCommentary(Long newsId, PageRequest page);

    CommentariesDto findCommentaryById(Long newsId, UserDetails userDetails, Long commentaryId);

    CommentariesDto createCommentary(Long newsId, UserDetails userDetails, CommentariesDto commentariesDto);

    CommentariesDto updateCommentaryById(Long newsId, Long commentaryId, UserDetails userDetails, CommentariesDto commentariesDto);

    CommentariesDto deleteCommentaryById(Long newsId, Long commentaryId, UserDetails userDetails);
}
