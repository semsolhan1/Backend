package com.handifarm.api.board.controller;

import com.handifarm.api.board.dto.request.BoardModifyRequestDTO;
import com.handifarm.api.board.dto.request.BoardWriteRequestDTO;
import com.handifarm.api.board.dto.response.BoardListResponseDTO;
import com.handifarm.api.board.service.BoardService;
import com.handifarm.jwt.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    // 게시글 전체 조회
    @GetMapping
    public ResponseEntity<?> retrieveBoard() {
        BoardListResponseDTO boardList = boardService.retrieve();
        return ResponseEntity.ok(boardList);
    }

    // 특정 사용자 게시글 목록 조회
    @GetMapping("/{userId}")
    public ResponseEntity<?> retrieveBoard(@PathVariable String userId) {
        BoardListResponseDTO boardList = boardService.retrieve(userId);
        return ResponseEntity.ok(boardList);
    }

    // 게시글 등록
    @PostMapping
    public ResponseEntity<?> registBoard(@RequestBody BoardWriteRequestDTO requestDTO, TokenUserInfo userInfo) {
        boardService.registBoard(requestDTO, userInfo);
        return ResponseEntity.ok("게시글등록성공");
    }

    // 게시글 수정
    @PutMapping("/{boardNo}")
    public ResponseEntity<?> updateBoard(@PathVariable long boardNo, @RequestBody BoardModifyRequestDTO requestDTO) {
        requestDTO.setBoardNo(boardNo);
        boardService.updateBoard(requestDTO);
        return ResponseEntity.ok().build();
    }

    // 게시글 삭제
    @DeleteMapping("/{boardNo}")
    public ResponseEntity<?> deleteBoard(@PathVariable long boardNo) {
        boardService.deleteBoard(boardNo);
        return ResponseEntity.ok().build();
    }





}
