package com.handifarm.api.snsBoard.controller;

import com.handifarm.api.board.dto.response.BoardListResponseDTO;
import com.handifarm.api.snsBoard.dto.request.SNSBoardCreateRequestDTO;
import com.handifarm.api.snsBoard.dto.request.SNSBoardModifyRequestDTO;
import com.handifarm.api.snsBoard.dto.response.SNSBoardListResponseDTO;
import com.handifarm.api.snsBoard.dto.response.SNSBoardResponseDTO;
import com.handifarm.api.snsBoard.service.SnsBoardService;
import com.handifarm.api.util.page.PageDTO;
import com.handifarm.jwt.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/sns")
public class SnsBoardController {

    private final SnsBoardService snsBoardService;

    @Autowired
    public SnsBoardController(SnsBoardService snsBoardService) {
        this.snsBoardService = snsBoardService;
    }

    // SNS 게시글 목록
    @GetMapping
    public ResponseEntity<?> getSnsList(PageDTO pageDTO) {
        log.info("SNS 게시글 목록 요청! - PageDTO : {}", pageDTO);
        SNSBoardListResponseDTO snsList = snsBoardService.getSnsList(pageDTO);
        return ResponseEntity.ok(snsList);
    }


    // SNS 게시글 조회
    @GetMapping("/{snsNo}")
    public ResponseEntity<?> getSns(@PathVariable long snsNo) {
        log.info("{}번 SNS 게시글 조회 요청!", snsNo);

        return null;
    }

    // SNS 게시글 등록
    @PostMapping
    public ResponseEntity<?> uploadSns(
            @AuthenticationPrincipal TokenUserInfo userInfo,
//            @Validated @RequestPart("snsContent") SNSBoardCreateRequestDTO dto,
//            @RequestPart(value = "snsImgs")List<MultipartFile> snsImgs,
            @Validated SNSBoardCreateRequestDTO dto,
            BindingResult result
            ) {
        log.info("SNS 게시글 등록 요청! - DTO : {}", dto);

        if (result.hasErrors()) {
            log.warn(result.toString());
            return ResponseEntity.badRequest().body(result.getFieldError());
        }

        try {
            SNSBoardResponseDTO snsBoardResponseDTO = snsBoardService.uploadSns(userInfo, dto, dto.getSnsImgs());
            return ResponseEntity.ok().body(snsBoardResponseDTO);
        } catch (Exception e) {
            log.error("SNS 게시글 등록 중 오류 발생", e);
            return ResponseEntity.badRequest().body("SNS 게시글 등록 중 오류 발생 : " + e.getMessage());
        }
    }

    // SNS 게시글 수정
    @PatchMapping("/{snsNo}")
    public ResponseEntity<?> modifySns(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @PathVariable long snsNo,
            @Validated SNSBoardModifyRequestDTO dto,
            BindingResult result
    ) {
        log.info("{}번 SNS 게시글 수정 요청! - DTO : {}", snsNo, dto);

        if (result.hasErrors()) {
            log.warn(result.toString());
            return ResponseEntity.badRequest().body(result.getFieldError());
        }

        return null;
    }

    // SNS 게시글 삭제
    @DeleteMapping("/{snsNo}")
    public ResponseEntity<?> deleteSns(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @PathVariable long snsNo) {
        log.info("{}번 SNS 게시글 삭제 요청!", snsNo);



        return null;
    }

}
