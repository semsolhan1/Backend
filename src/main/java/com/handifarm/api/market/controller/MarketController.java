package com.handifarm.api.market.controller;

import com.handifarm.api.market.dto.request.MarketItemCreateRequestDTO;
import com.handifarm.api.market.dto.request.MarketItemModifyRequestDTO;
import com.handifarm.api.market.service.MarketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/market")
public class MarketController {

    private final MarketService marketService;

    // 판매 게시글 목록 요청
    @GetMapping
    public ResponseEntity<?> getList() {
        log.info("판매 게시글 목록 요청!");
        return ResponseEntity.ok().body("서버에서 판매글 목록 요청 받음.");
    }

    // 판매 게시글 등록
    @PostMapping
    public ResponseEntity<?> resistItem(@RequestBody MarketItemCreateRequestDTO requestDTO) {
        log.info("판매 게시글 등록 요청!");
        return ResponseEntity.ok().body("서버에서 판매글 등록 요청 받음.");
    }

    // 판매 게시글 조회
    @GetMapping("/{itemNo}")
    public ResponseEntity<?> getItem(@PathVariable long itemNo) {
        log.info("{}번 판매 게시글 조회 요청!", itemNo);
        return ResponseEntity.ok().body("서버에서 " + itemNo + "번 판매글 조회 요청 받음.");
    }

    // 판매 게시글 수정
    @PatchMapping
    public ResponseEntity<?> modify(@RequestBody MarketItemModifyRequestDTO requestDTO) {
        log.info("{}번 판매 게시글 수정 요청!", requestDTO.getItemNo());
        return ResponseEntity.ok().body("서버에서 " + requestDTO.getItemNo() + "번 판매글 수정 요청 받음.");
    }

    // 판매 게시글 삭제
    @DeleteMapping("/{itemNo}")
    public ResponseEntity<?> delete(@PathVariable long itemNo) {
        log.info("{}번 판매 게시글 삭제 요청!", itemNo);
        return ResponseEntity.ok().body("서버에서 " + itemNo + "번 판매글 삭제 요청 받음");
    }

    // 판매 완료 처리
    @PatchMapping("/{itemNo}")
    public ResponseEntity<?> done(@PathVariable long itemNo) {
        log.info("{}번 판매 게시글 판매 완료 처리 요청!", itemNo);
        return ResponseEntity.ok().body("서버에서 " + itemNo + "번 판매글 판매 완료 처리 요청 받음");
    }

}
