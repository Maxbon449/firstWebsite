package com.maxbon.springFirst.domain.board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardResponseDTO {

    private String title;
    private String content;
    private Long id;
    private String nickname;
    private String boardRole;
}
