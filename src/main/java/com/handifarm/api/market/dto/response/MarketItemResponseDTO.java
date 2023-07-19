package com.handifarm.api.market.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.handifarm.api.market.entity.ItemImg;
import com.handifarm.api.market.entity.MarketItem;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Setter @Getter
@ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MarketItemResponseDTO {

    private long itemNo;
    private String seller;
    private String itemName;
    private String itemContent;
    private int price;
    private boolean done;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime regDate;

    private List<String> imgLinks;

    // 엔터티를 DTO로 변환
    public MarketItemResponseDTO(MarketItem marketItem) {
        this.itemNo = marketItem.getItemNo();
        this.seller = marketItem.getSeller();
        this.itemName = marketItem.getItemName();
        this.itemContent = marketItem.getItemContent();
        this.price = marketItem.getPrice();
        this.done = marketItem.isDone();
        this.regDate = marketItem.getUpdateDate() == null ? marketItem.getCreateDate() : marketItem.getUpdateDate();
        this.imgLinks = getImgLinks(marketItem.getItemImgs());
    }

    // 이미지 링크 리스트를 만드는 메서드
    private List<String> getImgLinks(List<ItemImg> itemImgs) {
//        List<String> imgLinks = new ArrayList<>();
//        itemImgs.forEach(item -> imgLinks.add(item.getImgLink()));
//        return imgLinks;
        return itemImgs.stream()
                .map(ItemImg::getImgLink)
                .collect(Collectors.toList());
    }

}
