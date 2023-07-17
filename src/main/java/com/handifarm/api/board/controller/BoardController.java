package com.handifarm.api.board.controller;

import com.handifarm.api.board.dto.request.BoardModifyRequestDTO;
import com.handifarm.api.board.dto.request.BoardWriteRequestDTO;
import com.handifarm.api.board.dto.response.BoardDetailResponseDTO;
import com.handifarm.api.board.dto.response.BoardListResponseDTO;
import com.handifarm.api.board.service.BoardService;
import com.handifarm.api.user.entity.User;
import com.handifarm.api.util.page.PageDTO;
import com.handifarm.jwt.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    // 게시글 전체 조회
    @GetMapping
    public ResponseEntity<?> getBoard(@RequestParam("page") int page, @RequestParam("size") int size) {
        PageDTO dto = new PageDTO(page, size);
        BoardListResponseDTO boardList = boardService.getPage(dto);
        return ResponseEntity.ok(boardList);
    }

    // 특정 게시글 조회
    @GetMapping("/{boardNo}")
    public ResponseEntity<?> detailBoard(@PathVariable long boardNo) {
        BoardDetailResponseDTO boardDetail = boardService.showDetailBoard(boardNo);
        log.info(boardDetail.toString());
        return ResponseEntity.ok(boardDetail);
    }

    // 게시글 등록
    @PostMapping
    public ResponseEntity<?> registBoard(@AuthenticationPrincipal TokenUserInfo userInfo,
                                         @RequestBody BoardWriteRequestDTO requestDTO) {
        boardService.registBoard(requestDTO, userInfo);
        return ResponseEntity.ok("게시글 등록 성공");
    }

    // 게시글 수정
    @CrossOrigin(origins = {"http://localhost:3000"})
    @PutMapping("/{boardNo}")
    public ResponseEntity<?> updateBoard(@PathVariable long boardNo, @RequestBody BoardModifyRequestDTO requestDTO) {
        requestDTO.setBoardNo(boardNo);
        boardService.updateBoard(requestDTO);
        return ResponseEntity.ok().build();
    }


    // 게시글 삭제
    @DeleteMapping("/{boardNo}")
    public ResponseEntity<?> deleteBoard(@PathVariable long boardNo,
                                         @AuthenticationPrincipal TokenUserInfo userInfo) {
        try {
            boardService.deleteBoard(boardNo, userInfo);
            return ResponseEntity.ok("게시글 삭제 성공");
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }







}
