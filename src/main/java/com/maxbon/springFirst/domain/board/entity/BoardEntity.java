package com.maxbon.springFirst.domain.board.entity;

import com.maxbon.springFirst.domain.comment.entity.CommentEntity;
import com.maxbon.springFirst.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private BoardRoleType boardRole;

    @ManyToOne
    private UserEntity userEntity;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    List<CommentEntity> commentEntityList = new ArrayList<>();

    // 게시글에 새로운 댓글 추가
    public void addCommentEntity(CommentEntity entity) {
        entity.setUserEntity(this);
        this.commentEntityList.add(entity);
    }

    // 댓글 삭제할 때 게시글과 연관관계 삭제
    public void removeCommentEntity(CommentEntity entity) {
        entity.setUserEntity(null);
        this.commentEntityList.remove(entity);
    }
}

