package com.maxbon.springFirst.domain.comment.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDTO {
    private String comment;
    private int up;
    private Long boardId;
}
