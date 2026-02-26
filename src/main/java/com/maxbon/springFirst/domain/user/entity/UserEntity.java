package com.maxbon.springFirst.domain.user.entity;

import com.maxbon.springFirst.domain.board.entity.BoardEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String nickname;

    @Enumerated(EnumType.STRING)
    private UserRoleType userRole;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    List<BoardEntity> boardEntityList = new ArrayList<>();

    // 유저에 새로운 글을 추가 할 때 (연관 관계 매핑)
    public void addBoardEntity(BoardEntity entity) {
        entity.setUserEntity(this);
        this.boardEntityList.add(entity);
    }

    // 유저의 기존 글을 삭제 할 때 (연관 관계 삭제)
    public void removeBoardEntity(BoardEntity entity) {
        entity.setUserEntity(null);
        this.boardEntityList.remove(entity);
    }
}
