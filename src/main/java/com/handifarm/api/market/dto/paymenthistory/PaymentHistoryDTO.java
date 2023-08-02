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

    private long itemNo;
    private String orderId;
    private long price;
    private String buyer;
    private String seller;
    private String orderName;

}
