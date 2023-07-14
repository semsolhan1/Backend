package com.handifarm.api.market.service;

import com.handifarm.api.market.dto.request.MarketItemCreateRequestDTO;
import com.handifarm.api.market.dto.request.MarketItemModifyRequestDTO;
import com.handifarm.api.market.dto.response.MarketItemListResponseDTO;
import com.handifarm.api.market.dto.response.MarketItemResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MarketService implements IMarketService {


    @Override
    public MarketItemListResponseDTO getItemList() {
        return null;
    }

    @Override
    public void registItem(MarketItemCreateRequestDTO requestDTO) {

    }

    @Override
    public MarketItemResponseDTO getItem(long itemNo) {
        return null;
    }

    @Override
    public MarketItemResponseDTO modifyItem(MarketItemModifyRequestDTO requestDTO) {
        return null;
    }

    @Override
    public void deleteItem(long itemNo) {

    }

    @Override
    public void doneItem(long itemNo) {

    }
}
