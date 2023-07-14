package com.handifarm.api.board.controller;

import com.handifarm.api.board.dto.request.BoardModifyRequestDTO;
import com.handifarm.api.board.dto.request.BoardReplyModifyRequestDTO;
import com.handifarm.api.board.dto.request.BoardReplyWriteRequestDTO;
import com.handifarm.api.board.service.BoardReplyService;
import com.handifarm.api.board.service.BoardService;
import com.handifarm.jwt.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardReplyController {

    private final BoardReplyService boardReplyService;

    // 댓글 등록
    @PostMapping
    public ResponseEntity<?> registBoardReply(@RequestBody BoardReplyWriteRequestDTO requestDTO, TokenUserInfo userInfo) {
        boardReplyService.registBoardReply(requestDTO, userInfo);
        return ResponseEntity.ok("댓글등록성공");
    }

    // 댓글 수정
    @PutMapping("/{boardNo}")
    public ResponseEntity<?> updateBoardReply(@PathVariable long boardReplyNo, @RequestBody BoardReplyModifyRequestDTO requestDTO) {
        boardReplyService.updateBoardReply(requestDTO);
        return ResponseEntity.ok().build();
    }

    // 댓글 삭제
    @DeleteMapping("/{boardNo}")
    public ResponseEntity<?> deleteBoardReply(@PathVariable long boardReplyNo) {
        boardReplyService.deleteBoardReply(boardReplyNo);
        return ResponseEntity.ok().build();
    }

}
