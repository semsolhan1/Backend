package com.handifarm.api.market.service;

import com.handifarm.api.market.dto.request.MarketItemCreateRequestDTO;
import com.handifarm.api.market.dto.request.MarketItemModifyRequestDTO;
import com.handifarm.api.market.dto.response.MarketItemListResponseDTO;
import com.handifarm.api.market.dto.response.MarketItemResponseDTO;

public interface IMarketService {

    // 판매 게시글 목록 요청
    MarketItemListResponseDTO getItemList();

    // 판매 게시글 등록
    void registItem(MarketItemCreateRequestDTO requestDTO);

    // 판매 게시글 조회
    MarketItemResponseDTO getItem(long itemNo);

    // 판매 게시글 수정
    MarketItemResponseDTO modifyItem(MarketItemModifyRequestDTO requestDTO);

    // 판매 게시글 삭제
    void deleteItem(long itemNo);

    // 판매 완료 처리
    void doneItem(long itemNo);

}
