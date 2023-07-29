package com.handifarm.api.snsBoard.controller;

import com.handifarm.api.snsBoard.dto.request.SnsBoardCreateRequestDTO;
import com.handifarm.api.snsBoard.dto.request.SnsBoardModifyRequestDTO;
import com.handifarm.api.snsBoard.dto.response.SnsBoardDetailListResponseDTO;
import com.handifarm.api.snsBoard.dto.response.SnsBoardListResponseDTO;
import com.handifarm.api.snsBoard.dto.response.SnsBoardResponseDTO;
import com.handifarm.api.snsBoard.service.SnsBoardService;
import com.handifarm.api.util.page.PageDTO;
import com.handifarm.jwt.TokenUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
        SnsBoardListResponseDTO snsList = snsBoardService.getSnsList(pageDTO);
        return ResponseEntity.ok(snsList);
    }


    // SNS 게시글 조회
    @GetMapping("/{snsNo}")
    public ResponseEntity<?> getSns(@PathVariable long snsNo, String userNick) {
        log.info("{}번 SNS 게시글 조회 요청! - 해당 게시글의 유저 : {}", snsNo, userNick);

        try {
            SnsBoardDetailListResponseDTO responseDTO = snsBoardService.getSns(snsNo, userNick);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            log.error("SNS 게시글 등록 중 오류 발생", e);
            return ResponseEntity.badRequest().body("SNS 게시글 등록 중 오류 발생 : " + e.getMessage());
        }

    }

    // SNS 게시글 등록
    @PostMapping
    public ResponseEntity<?> uploadSns(
            @AuthenticationPrincipal TokenUserInfo userInfo,
//            @Validated @RequestPart("snsContent") SnsBoardCreateRequestDTO dto,
//            @RequestPart(value = "snsImgs")List<MultipartFile> snsImgs,
            @Validated SnsBoardCreateRequestDTO dto,
            BindingResult result
            ) {
        log.info("SNS 게시글 등록 요청! - DTO : {}", dto);

        if (result.hasErrors()) {
            log.warn(result.toString());
            return ResponseEntity.badRequest().body(result.getFieldError());
        }

        try {
            SnsBoardResponseDTO snsBoardResponseDTO = snsBoardService.uploadSns(userInfo, dto, dto.getSnsImgs());
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
            @Validated SnsBoardModifyRequestDTO dto,
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
