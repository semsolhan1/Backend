package com.handifarm.api.market.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Setter @Getter
@ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MarketItemModifyRequestDTO {

    @NotBlank
    private String itemName;

    private String itemContent;

    @NotNull
    private int price;

    List<String> imgLinks;

}
