package com.handifarm.api.market.controller;

import com.handifarm.api.market.dto.request.MarketItemCreateRequestDTO;
import com.handifarm.api.market.dto.request.MarketItemModifyRequestDTO;
import com.handifarm.api.market.dto.response.MarketItemListResponseDTO;
import com.handifarm.api.market.dto.response.MarketItemResponseDTO;
import com.handifarm.api.market.service.MarketService;
import com.handifarm.api.util.page.PageDTO;
import com.handifarm.jwt.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/market")
public class MarketController {

    private final MarketService marketService;

    // 판매 게시글 목록
    @GetMapping
    public ResponseEntity<?> getList(PageDTO pageDTO) {
        log.info("판매 게시글 목록 요청! - PageDTO : {}", pageDTO);
        MarketItemListResponseDTO itemList = marketService.getItemList(pageDTO);
        return ResponseEntity.ok().body(itemList);
    }

    // 판매 게시글 등록
    @PostMapping
    public ResponseEntity<?> registItem(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @Validated @RequestPart("marketItem") MarketItemCreateRequestDTO requestDTO,
            @RequestPart(value = "itemImgs", required = false) List<MultipartFile> itemImgs,
            BindingResult result) {
        log.info("판매 게시글 등록 요청! - DTO : {}", requestDTO);

        if (result.hasErrors()) {
            log.warn(result.toString());
            return ResponseEntity.badRequest().body(result.getFieldError());
        }

        try {
            MarketItemResponseDTO marketItemResponseDTO = marketService.registItem(userInfo, requestDTO, itemImgs);
            return ResponseEntity.ok().body(marketItemResponseDTO);
        } catch (Exception e) {
            log.error("판매 게시글 등록 중 오류 발생", e);
            return ResponseEntity.badRequest().body("판매 게시글 등록 중 오류 발생 : " + e.getMessage());
        }
    }

    // 판매 게시글 조회
    @GetMapping("/{itemNo}")
    public ResponseEntity<?> getItem(@PathVariable long itemNo) {
        log.info("{}번 판매 게시글 조회 요청!", itemNo);
        MarketItemResponseDTO item = marketService.getItem(itemNo);
        return ResponseEntity.ok().body(item);
    }

    // 판매 게시글 수정
    @PatchMapping("/{itemNo}")
    public ResponseEntity<?> modify(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @PathVariable long itemNo,
            @Validated @RequestPart("itemInfo") MarketItemModifyRequestDTO requestDTO,
            @RequestPart(value = "itemImgs", required = false) List<MultipartFile> itemImgs,
            BindingResult result
    ) {
        log.info("{}번 판매 게시글 수정 요청! - DTO : {}", itemNo, requestDTO);

        if (result.hasErrors()) {
            log.warn(result.toString());
            return ResponseEntity.badRequest().body(result.getFieldError());
        }

        try {
            MarketItemResponseDTO marketItemResponseDTO = marketService.modifyItem(userInfo, itemNo, requestDTO, itemImgs);
            return ResponseEntity.ok().body(marketItemResponseDTO);
        } catch (RuntimeException e) {
            log.error("판매 게시글 수정 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("판매 게시글 수정 중 오류 발생: " + e.getMessage());
        }
    }

    // 판매 게시글 삭제
    @DeleteMapping("/{itemNo}")
    public ResponseEntity<?> delete(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @PathVariable long itemNo) {
        log.info("{}번 판매 게시글 삭제 요청!", itemNo);
        try {
            marketService.deleteItem(userInfo, itemNo);
            return ResponseEntity.ok().body("판매 게시글 삭제 완료");
        } catch (Exception e) {
            log.error("판매 게시글 삭제 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("판매 게시글 삭제 중 오류 발생: " + e.getMessage());
        }
    }

    // 판매 완료 처리
    @PatchMapping("/done/{itemNo}")
    public ResponseEntity<?> done(@PathVariable long itemNo) {
        log.info("{}번 판매 게시글 판매 완료 처리 요청!", itemNo);
        try {
            MarketItemResponseDTO marketItemResponseDTO = marketService.doneItem(itemNo);
            return ResponseEntity.ok().body(marketItemResponseDTO);
        } catch (Exception e) {
            log.error("판매 게시글 판매 완료 처리 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("판매 게시글 판매 완료 처리 중 오류 발생: " + e.getMessage());
        }
    }

}
