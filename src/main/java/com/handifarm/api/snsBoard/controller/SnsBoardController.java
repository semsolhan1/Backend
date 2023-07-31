package com.handifarm.api.snsBoard.controller;

import com.handifarm.api.snsBoard.dto.request.SnsBoardCreateRequestDTO;
import com.handifarm.api.snsBoard.dto.request.SnsBoardModifyRequestDTO;
import com.handifarm.api.snsBoard.dto.response.SnsBoardDetailListResponseDTO;
import com.handifarm.api.snsBoard.dto.response.SnsBoardListResponseDTO;
import com.handifarm.api.snsBoard.dto.response.SnsBoardResponseDTO;
import com.handifarm.api.snsBoard.service.SnsBoardService;
import com.handifarm.api.util.page.PageDTO;
import com.handifarm.jwt.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/sns")
public class SnsBoardController {

    private final SnsBoardService snsBoardService;

    // SNS 전체 게시글 목록
    @GetMapping
    public ResponseEntity<?> getSnsList(PageDTO pageDTO) {
        log.info("SNS 게시글 목록 요청! - PageDTO : {}", pageDTO);
        SnsBoardListResponseDTO snsList = snsBoardService.getSnsList(pageDTO);
        return ResponseEntity.ok(snsList);
    }


    // SNS 유저 게시글 목록
    @GetMapping("/{snsNo}")
    public ResponseEntity<?> getSns(@PathVariable(required = false) Long snsNo, String userNick) {
        log.info("{}번 SNS 게시글 조회 요청! - 해당 게시글의 작성자 : {}", snsNo, userNick);

        if (snsNo == null) snsNo = 0L;

        try {
            SnsBoardDetailListResponseDTO responseDTO = snsBoardService.getSnsUserList(snsNo, userNick);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            log.error("SNS 게시글 등록 중 오류 발생", e);
            return ResponseEntity.badRequest().body("SNS 게시글 등록 중 오류 발생 : " + e.getMessage());
        }

    }

    // SNS 게시글 등록
    @PostMapping
//    @Async
    public ResponseEntity<?> uploadSns(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @Validated @RequestPart("snsContent") SnsBoardCreateRequestDTO dto,
            @RequestPart(value = "snsImgs") List<MultipartFile> snsImgs,
            BindingResult result
            ) {
        log.info("SNS 게시글 등록 요청! - DTO : {}", dto);

        if (result.hasErrors()) {
            log.warn(result.toString());
            return ResponseEntity.badRequest().body(result.getFieldError());
        }

        try {
            SnsBoardResponseDTO snsBoardResponseDTO = snsBoardService.uploadSns(userInfo, dto, snsImgs);
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
            @Validated @RequestBody SnsBoardModifyRequestDTO dto,
            BindingResult result
    ) {
        log.info("{}번 SNS 게시글 수정 요청! - DTO : {}", snsNo, dto);

        if (result.hasErrors()) {
            log.warn(result.toString());
            return ResponseEntity.badRequest().body(result.getFieldError());
        }

        try {
            SnsBoardResponseDTO snsBoardResponseDTO = snsBoardService.modifySns(userInfo, snsNo, dto);
            return ResponseEntity.ok().body(snsBoardResponseDTO);
        } catch (Exception e) {
            log.error("SNS 게시글 수정 중 오류 발생", e);
            return ResponseEntity.badRequest().body("SNS 게시글 수정 중 오류 발생 : " + e.getMessage());
        }
    }

    // SNS 게시글 삭제
    @DeleteMapping("/{snsNo}")
    public ResponseEntity<?> deleteSns(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @PathVariable long snsNo) {
        log.info("{}번 SNS 게시글 삭제 요청!", snsNo);

        try {
            snsBoardService.deleteSns(userInfo, snsNo);
            return ResponseEntity.ok().body(snsNo + "번 게시글 삭제 성공!");
        } catch (Exception e) {
            log.error("SNS 게시글 삭제 중 오류 발생", e);
            return ResponseEntity.badRequest().body("SNS 게시글 삭제 중 오류 발생 : " + e.getMessage());
        }
    }

    // SNS 게시글 댓글 등록
    @PostMapping("/{snsNo}/reply")
    public ResponseEntity<?> snsReply(@AuthenticationPrincipal TokenUserInfo userInfo,
                                      @PathVariable long snsNo, String reply) {
        log.info("{}번 SNS 게시글 댓글 등록 요청! - Token User Info : {}", snsNo, userInfo);

        try {
            SnsBoardDetailListResponseDTO responseDTO = snsBoardService.registReply(userInfo, snsNo, reply);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            log.error("SNS 게시글 댓글 등록 중 오류 발생", e);
            return ResponseEntity.badRequest().body("SNS 게시글 댓글 등록 중 오류 발생 : " + e.getMessage());
        }
    }

    // SNS 게시글 댓글 수정
    @PatchMapping("/{snsNo}/reply/{replyNo}")
    public ResponseEntity<?> modifyReply(@AuthenticationPrincipal TokenUserInfo userInfo,
                                         @PathVariable long snsNo, @PathVariable long replyNo, String reply) {
        log.info("{}번 SNS 게시글 {}번 댓글 수정 요청! - Token User Info : {}", snsNo, replyNo, userInfo);

        try {
            SnsBoardDetailListResponseDTO responseDTO = snsBoardService.modifyReply(userInfo, snsNo, replyNo, reply);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            log.error("SNS 게시글 댓글 수정 중 오류 발생", e);
            return ResponseEntity.badRequest().body("SNS 게시글 댓글 수정 중 오류 발생 : " + e.getMessage());
        }
    }

    // SNS 게시글 댓글 삭제
    @DeleteMapping("/{snsNo}/reply/{replyNo}")
    public ResponseEntity<?> deleteReply(@AuthenticationPrincipal TokenUserInfo userInfo,
                                         @PathVariable long snsNo, @PathVariable long replyNo) {
        log.info("{}번 SNS 게시글 {}번 댓글 삭제 요청! - Token User Info : {}", snsNo, replyNo, userInfo);

        try {
            SnsBoardDetailListResponseDTO responseDTO = snsBoardService.deleteReply(userInfo, snsNo, replyNo);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            log.error("SNS 게시글 댓글 삭제 중 오류 발생", e);
            return ResponseEntity.badRequest().body("SNS 게시글 댓글 삭제 중 오류 발생 : " + e.getMessage());
        }
    }

    // SNS 게시글 좋아요
    @PatchMapping("/{snsNo}/like")
    public ResponseEntity<?> snsLike(@AuthenticationPrincipal TokenUserInfo userInfo,
                                     @PathVariable long snsNo) {
        log.info("{}번 SNS 게시글 좋아요 요청! - Token User Info : {}", snsNo, userInfo);

        try {
            SnsBoardDetailListResponseDTO responseDTO = snsBoardService.snsLike(userInfo, snsNo);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            log.error("SNS 게시글 좋아요 처리 중 오류 발생", e);
            return ResponseEntity.badRequest().body("SNS 게시글 좋아요 처리 중 오류 발생 : " + e.getMessage());
        }
    }

}
