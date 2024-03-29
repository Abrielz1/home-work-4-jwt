package ru.skillbox.homework4.commentary.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.homework4.commentary.dto.CommentariesDto;
import ru.skillbox.homework4.commentary.service.CommentaryService;
import ru.skillbox.homework4.common.Create;
import ru.skillbox.homework4.common.Update;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/news/{newsId}")
@RequiredArgsConstructor
public class CommentaryController {

    private final CommentaryService commentaryService;

    @GetMapping("/commentaries")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<CommentariesDto> findAll(
            @Positive @PathVariable(name = "newsId") Long newsId,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size) {

        return commentaryService.findAllCommentary(newsId, PageRequest.of(from / size, size));
    }

    @GetMapping("/commentaries/{commentaryId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public CommentariesDto findCommentaryById(
            @Positive @PathVariable(name = "newsId") Long newsId,
            UserDetails userDetails,
            @PathVariable Long commentaryId) {

        return commentaryService.findCommentaryById(newsId, userDetails, commentaryId);
    }

    @PostMapping("/commentaries")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public CommentariesDto createCommentary(
            @Positive @PathVariable(name = "newsId") Long newsId,
            UserDetails userDetails,
            @Validated(Create.class) @RequestBody CommentariesDto commentariesDto) {

        return commentaryService.createCommentary(newsId, userDetails, commentariesDto);
    }

    @PutMapping("/commentaries/{commentaryId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public CommentariesDto updateCommentaryById(
            @Positive @PathVariable(name = "newsId") Long newsId,
            UserDetails userDetails,
            @Positive @PathVariable Long commentaryId,
            @Validated(Update.class) @RequestBody CommentariesDto commentariesDto) {

        return commentaryService.updateCommentaryById(newsId, commentaryId, userDetails, commentariesDto);
    }

    @DeleteMapping("/commentaries/{commentaryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public CommentariesDto deleteCommentaryById(
            @Positive @PathVariable(name = "newsId") Long newsId,
            UserDetails userDetails,
            @PathVariable Long commentaryId) {

        return commentaryService.deleteCommentaryById(newsId, commentaryId, userDetails);
    }
}


