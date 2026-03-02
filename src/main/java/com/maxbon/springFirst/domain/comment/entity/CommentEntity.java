package com.maxbon.springFirst.domain.comment.entity;

import com.maxbon.springFirst.domain.board.entity.BoardEntity;
import com.maxbon.springFirst.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;
    private int up;

    @ManyToOne
    private UserEntity userEntity;

    @ManyToOne
    private BoardEntity boardEntity;
}
