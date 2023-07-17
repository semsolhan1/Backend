package com.handifarm.api.market.entity;

import lombok.*;

import javax.persistence.*;

@Setter @Getter
@ToString(exclude = "marketItem")
@EqualsAndHashCode()
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_item_img")
public class ItemImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long imgNo;

    @Column(nullable = false)
    private String imgLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_no")
    private MarketItem marketItem;

}
