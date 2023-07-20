package com.handifarm.api.market.service;

import com.handifarm.api.market.dto.request.MarketItemCreateRequestDTO;
import com.handifarm.api.market.dto.request.MarketItemModifyRequestDTO;
import com.handifarm.api.market.dto.response.MarketItemListResponseDTO;
import com.handifarm.api.market.dto.response.MarketItemResponseDTO;
import com.handifarm.api.market.entity.ItemImg;
import com.handifarm.api.market.entity.MarketItem;
import com.handifarm.api.market.repository.ItemImgRepository;
import com.handifarm.api.market.repository.MarketItemRepository;
import com.handifarm.api.util.page.PageDTO;
import com.handifarm.api.util.page.PageResponseDTO;
import com.handifarm.aws.S3Service;
import com.handifarm.jwt.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MarketService implements IMarketService {

    private final MarketItemRepository marketItemRepository;
    private final ItemImgRepository itemImgRepository;
    private final S3Service s3Service;

    // 판매 게시글 목록 요청
    @Override
    public MarketItemListResponseDTO getItemList(PageDTO dto) {

        //Pageable 객체 생성
        Pageable pageable = PageRequest.of(
                dto.getPage() - 1,
                dto.getSize(),
                Sort.by("createDate").descending()
        );

        Page<MarketItem> marketItems = marketItemRepository.findAll(pageable);

        List<MarketItem> itemList = marketItems.getContent();

        List<MarketItemResponseDTO> itemResponseDTOList = itemList.stream().map(MarketItemResponseDTO::new).collect(Collectors.toList());

        return MarketItemListResponseDTO.builder()
                .count(itemResponseDTOList.size())
                .pageInfo(new PageResponseDTO(marketItems))
                .marketItems(itemResponseDTOList)
                .build();
    }

    // 판매 게시글 등록
    @Override
    public MarketItemResponseDTO registItem(TokenUserInfo userInfo, MarketItemCreateRequestDTO requestDTO, List<MultipartFile> itemImgs) {

        if (!userInfo.getUserNick().equals(requestDTO.getSeller())) {
            throw new RuntimeException("인증이 유효하지 않습니다.");
        }

        String MARKET = "MARKET";

        MarketItem marketItem = marketItemRepository.save(requestDTO.toEntity());

        if(itemImgs != null && !itemImgs.isEmpty()) {
            List<String> uploadUrls = itemImgs.stream()
                    .map(itemImg -> {
                        try {
                            String uuidFileName = UUID.randomUUID() + "_" + itemImg.getOriginalFilename();
                            String uploadUrl = s3Service.uploadToS3Bucket(itemImg.getBytes(), uuidFileName, MARKET);
                            return uploadUrl;
                        } catch (IOException e) {
                            throw new RuntimeException("이미지 업로드에 실패하였습니다.", e);
                        }
                    })
                    .collect(Collectors.toList());
            uploadUrls.forEach(url -> {
                ItemImg savedItemImg = itemImgRepository.save(
                        ItemImg.builder()
                                .imgLink(url)
                                .marketItem(marketItem)
                                .build());
                marketItem.addItemImg(savedItemImg);
            });
        }

        return new MarketItemResponseDTO(marketItem);

    }

    // 판매 게시글 조회
    @Override
    public MarketItemResponseDTO getItem(long itemNo) {
        return null;
    }

    // 판매 게시글 수정
    @Override
    public MarketItemResponseDTO modifyItem(MarketItemModifyRequestDTO requestDTO) {
        return null;
    }

    // 판매 게시글 삭제
    @Override
    public void deleteItem(long itemNo) {

    }

    // 판매 완료 처리
    @Override
    public void doneItem(long itemNo) {

    }
}
