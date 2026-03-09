package com.maxbon.springFirst.domain.board.service;

import com.maxbon.springFirst.domain.board.dto.BoardRequestDTO;
import com.maxbon.springFirst.domain.board.dto.BoardResponseDTO;
import com.maxbon.springFirst.domain.board.entity.BoardEntity;
import com.maxbon.springFirst.domain.board.entity.BoardRoleType;
import com.maxbon.springFirst.domain.board.repository.BoardRepository;
import com.maxbon.springFirst.domain.user.entity.UserEntity;
import com.maxbon.springFirst.domain.user.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public BoardService(BoardRepository boardRepository, UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
    }

    // isAccess 만들고
    public boolean isAccess(Long id) {

        // 현재 로그인 된 유저의 username
        String sessionUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        // 현재 로그인 된 유저의 role
        String sessionRole = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority();

        // ADMIN 권한이 주어졌는지.
        if("ROLE_ADMIN".equals(sessionRole)){
            return true;
        }

        // 현재 로그인 된 유저의 username 과 해당 게시글을 작성한 username 이 동일한지
        String boardUsername = boardRepository.findById(id).orElseThrow().getUserEntity().getUsername();

        if(boardUsername.equals(sessionUsername)){
            return true;
        }

        return false;
    }

    // CREATE 게시글 하나 생성 (ROLE 타입 다르게 두 개 해야함)
    @Transactional
    public Long createOneBoard(BoardRequestDTO dto){
        BoardEntity entity = new BoardEntity();

        // 게시글 생성 로직
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setBoardRole(BoardRoleType.valueOf(dto.getBoardRole()));

        boardRepository.save(entity);

        // 유저와 게시글 매핑
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow();
        userEntity.addBoardEntity(entity);
        userRepository.save(userEntity);

        return entity.getId();
    }

    // READ 게시글 하나 읽기
    @Transactional(readOnly = true)
    public BoardResponseDTO readOneBoard(Long id){

        BoardEntity entity = boardRepository.findById(id).orElseThrow();

        BoardResponseDTO dto = new BoardResponseDTO();
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setId(entity.getId());
        dto.setNickname(entity.getUserEntity().getNickname());
        dto.setBoardRole(entity.getBoardRole().toString());

        return dto;
    }

    // 게시글 모두 읽기, RECOMMEND 만 모두, GATHER 만 모두
    @Transactional(readOnly = true)
    public List<BoardResponseDTO> readAllBoards(BoardRoleType boardRole){

        List<BoardEntity> list = (boardRole == null) ? boardRepository.findAll()
                : boardRepository.findByBoardRole(boardRole);

        List<BoardResponseDTO> dtos = new ArrayList<>();

        for(BoardEntity entity : list){
            BoardResponseDTO dto = new BoardResponseDTO();

            dto.setTitle(entity.getTitle());
            dto.setContent(entity.getContent());
            dto.setId(entity.getId());
            dto.setNickname(entity.getUserEntity().getNickname());
            dto.setBoardRole(entity.getBoardRole().toString());
            dtos.add(dto);
        }

        return dtos;
    }

    // UPDATE 게시글 하나 수정
    @Transactional
    public void updateOneBoard(BoardRequestDTO dto, Long id){

        BoardEntity entity = boardRepository.findById(id).orElseThrow();

        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setBoardRole(BoardRoleType.valueOf(dto.getBoardRole()));

        boardRepository.save(entity);
    }


    // DELETE 게시글 하나 삭제
    @Transactional
    public void deleteOneBoard(Long id){
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow();
        UserEntity userEntity = boardEntity.getUserEntity();

        userEntity.removeBoardEntity(boardEntity);
        boardRepository.deleteById(id);
    }
}
