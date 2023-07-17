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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestScope;
import retrofit2.http.Path;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/abi/boardReply")
public class BoardReplyController {

    private final BoardReplyService boardReplyService;

    @GetMapping
    public ResponseEntity<?> getBoardReply(@RequestParam("page") int page, @RequestParam("size") int size) {
        PageDTO dto = new PageDTO(page, size);
        BoardReplyListResponseDTO boardReplyList = boardReplyService.getPage(dto);
        return ResponseEntity.ok(boardReplyList);
    }

    // 댓글 등록
    @PostMapping
    public ResponseEntity<?> registBoardReply(@AuthenticationPrincipal TokenUserInfo userInfo,
                                              @RequestBody BoardReplyWriteRequestDTO requestDTO) {
        boardReplyService.registBoardReply(requestDTO, userInfo);
        return ResponseEntity.ok("댓글 등록 성공");
    }

    // 댓글 수정
    @PutMapping("/{boardNo}/{replyNo}")
    public ResponseEntity<?> updateBoardReply(@PathVariable long boardNo,
                                              @PathVariable long replyNo,
                                              @RequestBody BoardReplyModifyRequestDTO requestDTO) {
        requestDTO.setBoardNo(boardNo);
        requestDTO.setBoardReplyNo(replyNo);
        return ResponseEntity.ok().build();
    }


}
