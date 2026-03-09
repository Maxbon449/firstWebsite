package com.maxbon.springFirst.controller;

import com.maxbon.springFirst.domain.comment.dto.CommentRequestDTO;
import com.maxbon.springFirst.domain.comment.dto.CommentResponseDTO;
import com.maxbon.springFirst.domain.comment.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 달기 : 수행
    @PostMapping("/board/{id}/comment")
    @ResponseBody
    public CommentResponseDTO createComment(@PathVariable Long id, @RequestBody CommentRequestDTO dto) {
        dto.setBoardId(id);
        commentService.createOneComment(dto);
    }

    // 댓글 수정
    // 댓글 삭제
    // 댓글 추천


}
