package com.handifarm.api.market.dto.request;

import com.handifarm.api.market.entity.MarketItem;
import lombok.*;

@Setter @Getter
@ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MarketItemCreateRequestDTO {

    private String seller;
    private String itemName;
    private String itemContent;
    private int price;

    public MarketItem toEntity() {
        return MarketItem.builder()
                .seller(this.seller)
                .itemName(this.itemName)
                .itemContent(this.itemContent)
                .price(this.price)
                .build();
    }

}
