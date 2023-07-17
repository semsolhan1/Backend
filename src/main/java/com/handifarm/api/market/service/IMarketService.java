package com.handifarm.api.market.service;

import com.handifarm.api.market.dto.request.MarketItemCreateRequestDTO;
import com.handifarm.api.market.dto.request.MarketItemModifyRequestDTO;
import com.handifarm.api.market.dto.response.MarketItemListResponseDTO;
import com.handifarm.api.market.dto.response.MarketItemResponseDTO;
import com.handifarm.api.util.page.PageDTO;
import com.handifarm.jwt.TokenUserInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IMarketService {

    // 판매 게시글 목록 요청
    MarketItemListResponseDTO getItemList(PageDTO pageDTO);

    // 판매 게시글 등록
    MarketItemResponseDTO registItem(TokenUserInfo userInfo, MarketItemCreateRequestDTO requestDTO, List<MultipartFile> itemImgs);

    // 판매 게시글 조회
    MarketItemResponseDTO getItem(long itemNo);

    // 판매 게시글 수정
    MarketItemResponseDTO modifyItem(TokenUserInfo userInfo, MarketItemModifyRequestDTO requestDTO);

    // 판매 게시글 삭제
    void deleteItem(TokenUserInfo userInfo, long itemNo);

    // 판매 완료 처리
    void doneItem(long itemNo);

}
