package com.maxbon.springFirst.domain.board.entity;

import lombok.Getter;

@Getter
public enum BoardRoleType {
    RECOMMEND("추천 게시판"),
    GATHER("구인 게시판");

    private final String boardType;

    BoardRoleType(String boardType) {
        this.boardType = boardType;
    }
}
