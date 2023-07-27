package com.handifarm.api.market.dto.response;

import com.handifarm.api.util.page.PageResponseDTO;
import lombok.*;

import java.util.List;

@Setter @Getter
@ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MarketItemListResponseDTO {

    private int count;
    private PageResponseDTO<?> pageInfo;
    private List<MarketItemResponseDTO> marketItems;

}
