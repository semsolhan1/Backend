package com.handifarm.api.board.controller;

import com.handifarm.api.board.Service.BoardService;
import com.handifarm.api.board.dto.*;
import com.handifarm.api.board.entity.Board;
import com.handifarm.api.board.entity.Category;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Tag(name = "board API", description = "게시물 조회, 등록 및 수정, 삭제 api")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/ex/boards")
public class BoardApiController {

  private BoardService boardService;

  // 게시글 목록 조회
  @GetMapping
  public ResponseEntity<?> list(BoardDTO boardDTO) {
    log.info("/api/ex/boards?page={}&size={}", boardDTO.getPage(), boardDTO.getSize());

    BoardListResponseDTO dto = boardService.getBoards(boardDTO);

    return ResponseEntity.ok().body(dto);
  }

  // 게시글 상세보기
  @GetMapping("/{board_no}")
  public ResponseEntity<?> detail(@PathVariable long board_no) {
    log.info("/api/ex/board/{}: GET", board_no);

    try {
      BoardDetailResponseDTO dto = boardService.getDetail(board_no);
      return ResponseEntity.ok().body(dto);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }

  }

  @Operation(summary = "게시물 작성", description = "게시물 작성을 담당하는 메서드")
  @Parameters(
          {
                  @Parameter(name = "userId", description = "게시물의 작성자 이름을 쓰세요.", example = "김뽀삐", required = true),
                  @Parameter(name = "title", description = "게시물의 제목을 쓰세요!", example = "제목제목", required = true),
                  @Parameter(name = "content", description = "게시물의 내용을 쓰세요!", example = "내용내용")
          }
  )
//게시글 등록
  @PostMapping
  public ResponseEntity<?> create(
          @Validated @RequestBody BoardCreateDTO dto
          , BindingResult result
  ) {

    log.info("/api/ex/boards: BOARD - payload: {}", dto);

    if (dto == null) {
      return ResponseEntity
              .badRequest()
              .body("등록 게시물 정보를 전달해 주세요!");
    }

    try {
      BoardDetailResponseDTO responseDTO = boardService.insert(dto);
      return ResponseEntity.ok()
              .body(responseDTO);
    } catch (RuntimeException e) {
      e.printStackTrace();
      return ResponseEntity
              .internalServerError()
              .body("서버 터진 원인: " + e.getMessage());
    }


  }

  //게시물 수정
  @RequestMapping(method = {RequestMethod.PATCH, RequestMethod.PUT})
  public ResponseEntity<?> update(
          @Validated @RequestBody boardModifyDTO dto,
          BindingResult result,
          HttpServletRequest request) {
    log.info("/api/ex/boars {} - dto: {}"
            , request.getMethod(), dto);

    BoardDetailResponseDTO resonseDTO = boardService.modify(dto);

    return ResponseEntity.ok().body(resonseDTO);
  }

  //게시물 삭제
  @DeleteMapping("/{board_no}")
  public ResponseEntity<?> delete(@PathVariable long board_no) {
    log.info("/api/ex/boards/{} DELETE!", board_no);

    try {
      boardService.delete(board_no);
      return ResponseEntity
              .ok("DEL SUCCESS!");
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity
              .internalServerError()
              .body(e.getMessage());
    }

  }
}


