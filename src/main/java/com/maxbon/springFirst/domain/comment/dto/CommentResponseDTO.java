package com.maxbon.springFirst.domain.comment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponseDTO {

    private Long id;
    private String comment;
    private int up;
    private String nickname;
}
