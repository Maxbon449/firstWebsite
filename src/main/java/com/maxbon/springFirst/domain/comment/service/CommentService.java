package com.maxbon.springFirst.domain.comment.service;

import com.maxbon.springFirst.domain.board.entity.BoardEntity;
import com.maxbon.springFirst.domain.board.repository.BoardRepository;
import com.maxbon.springFirst.domain.comment.dto.CommentRequestDTO;
import com.maxbon.springFirst.domain.comment.dto.CommentResponseDTO;
import com.maxbon.springFirst.domain.comment.entity.CommentEntity;
import com.maxbon.springFirst.domain.comment.repository.CommentRepository;
import com.maxbon.springFirst.domain.user.entity.UserEntity;
import com.maxbon.springFirst.domain.user.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, BoardRepository boardRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
    }

    // isAccess 권한 확인
    public boolean isAccess(Long id){

        // 현재 로그인 된 유저의 username
        String sessionUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        // 현재 로그인 된 유저의 role
        String sessionRole = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority();

        // ADMIN 권한이 주어졌는지.
        if("ROLE_ADMIN".equals(sessionRole)){
            return true;
        }

        String username = commentRepository.findById(id).orElseThrow().getUserEntity().getUsername();

        // 현재 로그인 한 유저가 접근하려는 정보의 유저와 동일 인물인지.
        if(sessionUsername.equals(username)){
            return true;
        }

        return false;
    }

    // Create 댓글 생성
    @Transactional
    public void createOneComment(CommentRequestDTO dto){

        // 댓글 생성 로직
        CommentEntity entity = new CommentEntity();
        entity.setContent(dto.getComment());
        entity.setUp(0);
        commentRepository.save(entity);

        // 댓글 유저 매핑
        String sessionUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByUsername(sessionUsername).orElseThrow();
        userEntity.addCommentEntity(entity);
        userRepository.save(userEntity);

        // 댓글 게시글 매핑
        BoardEntity boardEntity = boardRepository.findById(dto.getBoardId()).orElseThrow();
        boardEntity.addCommentEntity(entity);
        boardRepository.save(boardEntity);
    }

    // Read 댓글 읽기
    // 댓글 하나 읽기
    @Transactional(readOnly = true)
    public CommentResponseDTO readOneComment(Long id){
        CommentEntity entity = commentRepository.findById(id).orElseThrow();
        CommentResponseDTO dto = new CommentResponseDTO();

        dto.setComment(entity.getContent());
        dto.setUp(entity.getUp());
        dto.setNickname(entity.getUserEntity().getNickname());

        return dto;
    }

    // 해당 게시글 댓글 모두 읽기
    @Transactional(readOnly = true)
    public List<CommentResponseDTO> readAllComments(Long boardId){
        BoardEntity boardEntity = boardRepository.findById(boardId).orElseThrow();
        List<CommentEntity> list = boardEntity.getCommentEntityList();
        List<CommentResponseDTO> dtos = new ArrayList<>();

        for(CommentEntity entity : list){
            CommentResponseDTO dto = new CommentResponseDTO();
            dto.setComment(entity.getContent());
            dto.setUp(entity.getUp());
            dto.setNickname(entity.getUserEntity().getNickname());
            dtos.add(dto);
        }

        return dtos;
    }

    // Update 댓글 수정, 댓글 추천
    // 댓글 수정
    @Transactional
    public void updateOneComment(CommentRequestDTO dto, Long id){
        CommentEntity entity = commentRepository.findById(id).orElseThrow();

        entity.setContent(dto.getComment());
        commentRepository.save(entity);
    }

    // 댓글 추천
    @Transactional
    public void upComment(Long id){
        CommentEntity entity = commentRepository.findById(id).orElseThrow();
        entity.setUp(entity.getUp() + 1);
        commentRepository.save(entity);
    }

    // Delete 댓글 삭제
    @Transactional
    public void deleteOneComment(Long id){
        CommentEntity entity = commentRepository.findById(id).orElseThrow();

        // 유저와의 연관관계 삭제
        UserEntity userEntity = entity.getUserEntity();
        userEntity.removeCommentEntity(entity);
        userRepository.save(userEntity);

        // 게시글과의 연관관계 삭제
        BoardEntity boardEntity = entity.getBoardEntity();
        boardEntity.removeCommentEntity(entity);
        boardRepository.save(boardEntity);

        // 댓글 삭제
        commentRepository.delete(entity);
    }
}
