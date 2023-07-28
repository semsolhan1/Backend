package com.handifarm.api.market.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter @Getter
@ToString @EqualsAndHashCode(of = "resultCode")
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_payment_history")
public class PaymentHistory {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String resultCode;

    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private String buyer;

    @Column(nullable = false)
    private String seller;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private int price;

    @CreationTimestamp
    private LocalDateTime paymentDate;

}
