package com.handifarm.api.market.dto.request;

import com.handifarm.api.market.entity.MarketItem;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter @Getter
@ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MarketItemCreateRequestDTO {

    @NotBlank
    private String seller;

    @NotBlank
    private String itemName;

    private String itemContent;

    @NotNull
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
