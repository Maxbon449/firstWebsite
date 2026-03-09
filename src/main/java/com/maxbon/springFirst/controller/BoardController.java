package com.maxbon.springFirst.controller;

import com.maxbon.springFirst.domain.board.dto.BoardRequestDTO;
import com.maxbon.springFirst.domain.board.dto.BoardResponseDTO;
import com.maxbon.springFirst.domain.board.entity.BoardRoleType;
import com.maxbon.springFirst.domain.board.service.BoardService;
import com.maxbon.springFirst.domain.comment.dto.CommentResponseDTO;
import com.maxbon.springFirst.domain.comment.service.CommentService;
import com.maxbon.springFirst.domain.user.auth.PrincipalDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;

    public BoardController(BoardService boardService, CommentService commentService) {
        this.boardService = boardService;
        this.commentService = commentService;
    }

    // 전체 게시판 : 페이지 응답
    @GetMapping("/board")
    public String boardPage(Model model) {

        List<BoardResponseDTO> dtos = boardService.readAllBoards(null);
        model.addAttribute("boards", dtos);

        return "boardMain";
    }

    // 추천 게시판 : 페이지 응답
    @GetMapping("/board/request")
    public String requestPage(Model model) {

        List<BoardResponseDTO> dtos = boardService.readAllBoards(BoardRoleType.RECOMMEND);
        model.addAttribute("boards", dtos);

        return "boardRequest";
    }

    // 구인 게시판 : 페이지 응답
    @GetMapping("/board/gather")
    public String gatherPage(Model model) {

        List<BoardResponseDTO> dtos = boardService.readAllBoards(BoardRoleType.GATHER);
        model.addAttribute("boards", dtos);

        return "boardGather";
    }

    // 글 생성 : 페이지 응답
    @GetMapping("/board/create")
    public String createBoardPage() {

        return "boardCreate";
    }

    // 글 생성 : 수행
    @PostMapping("/board/create")
    public String createBoard(BoardRequestDTO dto) {

        Long id = boardService.createOneBoard(dto);

        return "redirect:/board/" + id;
    }


    // 글 읽기 : 페이지 응답
    @GetMapping("/board/{id}")
    public String readIdPage(Model model, @PathVariable Long id) {
        BoardResponseDTO dto = boardService.readOneBoard(id);
        model.addAttribute("board", dto);
        List<CommentResponseDTO> comments = commentService.readAllComments(id);
        model.addAttribute("comments", comments);

        return "readIdBoard";
    }

    //글 수정 : 페이지 응답
    @GetMapping("/board/update/{id}")
    public String updateBoardPage(@PathVariable Long id, Model model, HttpServletRequest request) {

        // 본인과 어드민만 수정 가능.
        if(boardService.isAccess(id)){
            BoardResponseDTO dto = boardService.readOneBoard(id);
            model.addAttribute("board", dto);

            return "boardUpdate";
        }

        String previousPage = request.getParameter("Referer");
        if(previousPage != null){
            return "redirect:" + previousPage;
        }

        return "redirect:/board";
    }

    //글 수정 : 수행
    @PostMapping("/board/update/{id}")
    public String updateBoard(@PathVariable Long id, BoardRequestDTO dto) {

        if(boardService.isAccess(id)){
            boardService.updateOneBoard(dto, id);

            return "redirect:/board/" + id;
        }

        return "redirect:/board";
    }

    //글 삭제 : 수행
    @PostMapping("/board/delete/{id}")
    public String deleteBoard(@PathVariable Long id) {

        if(boardService.isAccess(id))
            boardService.deleteOneBoard(id);

        // 삭제 하면 전체 페이지로 이동
        return "redirect:/board";
    }
}
