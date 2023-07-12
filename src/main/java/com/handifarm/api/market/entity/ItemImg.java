package com.handifarm.api.market.entity;

import javax.persistence.*;

@Entity
public class ItemImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long imgNo;

    @Column(nullable = false)
    private String imgLink;

    private long itemNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_no")
    private MarketItem marketItem;

}
