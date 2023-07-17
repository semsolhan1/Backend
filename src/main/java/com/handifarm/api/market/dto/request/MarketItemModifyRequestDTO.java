package com.handifarm.api.market.dto.request;

import lombok.*;

import java.util.List;

@Setter @Getter
@ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MarketItemModifyRequestDTO {

    private long itemNo;
    private String itemName;
    private String itemContent;
    private int price;

    List<String> imgLinks;

}
