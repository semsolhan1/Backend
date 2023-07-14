package com.handifarm.api.market.dto.request;

import lombok.*;

@Setter @Getter
@ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MarketItemCreateRequestDTO {

    private long itemNo;

}
