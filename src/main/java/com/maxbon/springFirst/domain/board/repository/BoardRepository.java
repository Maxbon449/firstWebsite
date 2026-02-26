package com.maxbon.springFirst.domain.board.repository;

import com.maxbon.springFirst.domain.board.entity.BoardEntity;
import com.maxbon.springFirst.domain.board.entity.BoardRoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    List<BoardEntity> findByBoardRole(BoardRoleType boardRole);
}
