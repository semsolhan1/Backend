package com.handifarm.api.market.dto.paymenthistory;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentHistoryDTO {

    private String orderId;
    private int amount;
    private String buyer;
    private String seller;
    private String orderName;

}
