package com.handifarm.api.market.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class TransHistory {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String resultCode;

    @Column(nullable = false)
    private String buyer;

    @Column(nullable = false)
    private String seller;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private int price;

    @CreationTimestamp
    private LocalDateTime transDate;

}
