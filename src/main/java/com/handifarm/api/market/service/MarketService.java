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
import java.util.ArrayList;
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
    public MarketItemListResponseDTO getItemList(final PageDTO dto) {

        //Pageable 객체 생성
        Pageable pageable = PageRequest.of(
                dto.getPage() - 1,
                dto.getSize(),
                Sort.by("createDate").descending()
        );

        Page<MarketItem> marketItems = marketItemRepository.findAll(pageable);

        List<MarketItem> itemList = marketItems.getContent();

        List<MarketItemResponseDTO> itemResponseDTOList =
                itemList.stream().map(MarketItemResponseDTO::new).collect(Collectors.toList());

        return MarketItemListResponseDTO.builder()
                .count(itemResponseDTOList.size())
                .pageInfo(new PageResponseDTO(marketItems))
                .marketItems(itemResponseDTOList)
                .build();
    }

    // 판매 게시글 등록
    @Override
    public MarketItemResponseDTO registItem(
            final TokenUserInfo userInfo,
            final MarketItemCreateRequestDTO requestDTO,
            final List<MultipartFile> itemImgs) {

        if (!userInfo.getUserNick().equals(requestDTO.getSeller())) {
            throw new RuntimeException("인증이 유효하지 않습니다.");
        }

        MarketItem marketItem = marketItemRepository.save(requestDTO.toEntity());

        addImgsToDBAndS3(itemImgs, marketItem);

        return new MarketItemResponseDTO(marketItem);

    }

    // 판매 게시글 조회
    @Override
    public MarketItemResponseDTO getItem(final long itemNo) {
        MarketItem marketItem = marketItemRepository.findById(itemNo)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게시글 번호입니다."));
        return new MarketItemResponseDTO(marketItem);
    }

    // 판매 게시글 수정
    @Override
    public MarketItemResponseDTO modifyItem(final TokenUserInfo userInfo, final long itemNo, final MarketItemModifyRequestDTO requestDTO, final List<MultipartFile> itemImgs) {
        MarketItem marketItem = marketItemRepository.findById(itemNo)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게시글 번호입니다."));

        if (!userInfo.getUserNick().equals(marketItem.getSeller())) {
            throw new RuntimeException("권한이 없습니다.");
        }

        // 새로운 데이터로 MarketItem 엔티티를 업데이트합니다.
        marketItem.setItemName(requestDTO.getItemName());
        marketItem.setItemContent(requestDTO.getItemContent());
        marketItem.setPrice(requestDTO.getPrice());

        // 이미지 링크를 처리합니다.
        List<ItemImg> existingItemImgs = marketItem.getItemImgs();
        List<String> newImgLinks = requestDTO.getImgLinks();

        // 삭제된 이미지를 찾아 S3 버킷과 데이터베이스에서 삭제합니다.
        List<ItemImg> removedItemImgs = new ArrayList<>();
        for (ItemImg existingItemImg : existingItemImgs) {
            if (!newImgLinks.contains(existingItemImg.getImgLink())) {
                // S3 버킷에서 이미지 삭제
                s3Service.deleteFromS3Bucket(existingItemImg.getImgLink());
                // 데이터베이스에서 이미지 삭제
                itemImgRepository.delete(existingItemImg);
                // 동기화를 위해 삭제된 파일 목록 추가
                removedItemImgs.add(existingItemImg);
            }
        }

        // marketItem의 itemImgs에서 삭제된 이미지를 제거합니다.
        marketItem.getItemImgs().removeAll(removedItemImgs);

        // 새로운 이미지를 데이터베이스와 S3 버킷에 추가합니다.
        addImgsToDBAndS3(itemImgs, marketItem);

        // 수정된 MarketItem을 데이터베이스에 저장합니다.
        MarketItem savedMarketItem = marketItemRepository.save(marketItem);

        return new MarketItemResponseDTO(savedMarketItem);
    }

    // 이미지 List DB와 S3 버킷에 추가하는 메서드
    private void addImgsToDBAndS3(List<MultipartFile> itemImgs, MarketItem marketItem) {
        String serviceName = "MARKET";

        if (itemImgs != null && !itemImgs.isEmpty()) {
            List<String> uploadUrls = itemImgs.stream()
                    .map(itemImg -> {
                        try {
                            String uuidFileName = UUID.randomUUID() + "_" + itemImg.getOriginalFilename();
                            String uploadUrl = s3Service.uploadToS3Bucket(itemImg.getBytes(), uuidFileName, serviceName);
                            return uploadUrl;
                        } catch (IOException e) {
                            log.error("이미지 업로드에 실패하였습니다.", e);
                            throw new RuntimeException("이미지 업로드에 실패하였습니다.");
                        }
                    })
                    .collect(Collectors.toList());

            // 데이터베이스와 MarketItem을 연결하며, 새로운 이미지들을 추가합니다.
            uploadUrls.forEach(url -> {
                ItemImg savedItemImg = itemImgRepository.save(ItemImg.builder()
                        .imgLink(url)
                        .marketItem(marketItem)
                        .build());
                marketItem.addItemImg(savedItemImg);
            });
        }
    }

    // 판매 게시글 삭제
    @Override
    public void deleteItem(final TokenUserInfo userInfo, final long itemNo) {
        MarketItem marketItem = marketItemRepository.findById(itemNo)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게시글 번호입니다."));

        if (!userInfo.getUserNick().equals(marketItem.getSeller())) {
            throw new RuntimeException("권한이 없습니다.");
        }

        // MarketItem에 연결된 이미지들을 가져와서 삭제합니다.
        List<ItemImg> itemImgs = marketItem.getItemImgs();
        for (ItemImg itemImg : itemImgs) {
            try {
                // S3 버킷에서 이미지 삭제
                s3Service.deleteFromS3Bucket(itemImg.getImgLink());
                // 데이터베이스에서 이미지 삭제
                itemImgRepository.delete(itemImg);
            } catch (Exception e) {
                log.error("이미지 삭제 실패 - itemImg: {}, err: {}", itemImg.getImgLink(), e.getMessage());
                throw new RuntimeException("이미지 삭제 도중 예외가 발생했습니다.");
            }
        }

        try {
            marketItemRepository.deleteById(itemNo);
        } catch (Exception e) {
            log.error("존재하지 않는 게시글 번호로 삭제가 실패했습니다. - itemNo : {}, err : {}", itemNo, e.getMessage());
            throw new RuntimeException("게시글이 존재하지 않아 삭제에 실패했습니다.");
        }
    }

    // 판매 완료 처리
    @Override
    public MarketItemResponseDTO doneItem(long itemNo) {
        MarketItem marketItem = marketItemRepository.findById(itemNo)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게시글 번호입니다."));

        // 판매 완료 처리를 이미 했는지 확인
        if (marketItem.isDone()) {
            throw new RuntimeException("이미 판매 완료된 게시글입니다.");
        }

        // 판매 완료 처리: done 값을 true로 설정
        marketItem.setDone(true);

        // 엔티티를 저장하여 변경사항을 데이터베이스에 반영
        marketItemRepository.save(marketItem);

        // MarketItemResponseDTO 객체를 생성하여 반환
        return new MarketItemResponseDTO(marketItem);
    }
}
