package com.handifarm.api.reply.controller;


import com.handifarm.api.board.dto.request.BoardWriteRequestDTO;
import com.handifarm.api.reply.dto.request.BoardReplyModifyRequestDTO;
import com.handifarm.api.reply.dto.request.BoardReplyWriteRequestDTO;
import com.handifarm.api.reply.dto.response.BoardReplyListResponseDTO;
import com.handifarm.api.reply.service.BoardReplyService;
import com.handifarm.api.util.page.PageDTO;
import com.handifarm.jwt.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestScope;
import retrofit2.http.Path;

import javax.persistence.EntityNotFoundException;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/board/{boardNo}/boardReply")
public class BoardReplyController {

    private final BoardReplyService boardReplyService;

    @GetMapping
    public ResponseEntity<?> getBoardReply(@ModelAttribute PageDTO pageDTO) {
        BoardReplyListResponseDTO boardReplyList = boardReplyService.getPage(pageDTO);
        return ResponseEntity.ok(boardReplyList);
    }

    // 댓글 등록
    @PostMapping
    public ResponseEntity<?> registBoardReply(@AuthenticationPrincipal TokenUserInfo userInfo,
                                              @PathVariable long boardNo,
                                              @RequestBody BoardReplyWriteRequestDTO requestDTO) {
        // 댓글 등록 로직 구현
        boardReplyService.registBoardReply(boardNo, requestDTO, userInfo);
        return ResponseEntity.ok("댓글 등록 성공");
    }

    // 댓글 수정
    @PutMapping("/{replyNo}")
    public ResponseEntity<?> updateBoardReply(@PathVariable long replyNo, @RequestBody BoardReplyModifyRequestDTO requestDTO) {
        try {
            boardReplyService.updateBoardReply(requestDTO);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // 댓글 삭제
    @DeleteMapping("/{replyNo}")
    public ResponseEntity<?> deleteBoardReply(@PathVariable long boardNo, @PathVariable long replyNo, @AuthenticationPrincipal TokenUserInfo userInfo) {
        try {
            boardReplyService.deleteBoardReply(replyNo, userInfo);
            return ResponseEntity.ok("댓글 삭제 성공");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

}

