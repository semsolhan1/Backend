package com.handifarm.api.board.controller;

import com.handifarm.api.board.dto.response.BoardListResponseDTO;
import com.handifarm.api.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    //게시판 목록 요청
    public ResponseEntity<?> retrieveBoardList() {
        log.info("/api/board GET request");
        BoardListResponseDTO responseDTO = boardService.retrieve();

        return ResponseEntity.ok().body(responseDTO);
    }

    //게시판 등록 요청
    public ResponseEntity<?> createBoard(
            @Validated @RequestBody BoardCreateRequestDTO requestDTO,
            bindingResult result
    ) {
        if (result.hasErrors()) {
            log.warn("DTO 검증 에러 발생");
            return ResponseEntity.badRequest().body(result.getFieldError());
        }
        try {
            BoardListResponseDTO responseDTO = boardService.create(requestDTO);
            return ResponseEntity.ok().body(responseDTO);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(BoardListResponseDTO.builder().error(e.getMessage()));
        }

    }


    //게시판 삭제 요청
    @DeleteMapping("/{boardNo}")
    public ResponseEntity<?> deleteBoard(
            @PathVariable("boardNo") String boardNo
    ) {
        log.info("/api/board/{} DELETE 리퀘스트", boardNo);
        if(boardNo == null || boardNo.trim().equals("")) {
            return ResponseEntity.badRequest()
                    .body(BoardListResponseDTO.builder().error("ID를 전달해주세요"));
        }

        try {
            BoardListResponseDTO responseDTO = todoService.delete(boardNo);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(BoardListResponseDTO.builder().error(e.getMessage()));
        }

    }


    //게시판 수정 요청
    @RequestMapping(method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<?> updateBoard(
            @Validated @RequestBody BoardModifyRequestDTO requestDTO, BindingResult result, HttpServletRequest request)
    {
        if(result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldError());
        }

        log.info("/api/board {} request!", request.getMethod());
        log.info("modifying dto: {}", requestDTO.toString());

        try {
            BoardListResponseDTO responseDTO = boardService.update(requestDTO);
            return ResponseEntity.ok().body(responseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError()
                    .body(BoardListResponseDTO.builder().error(e.getMessage()));
        }
    }



}
