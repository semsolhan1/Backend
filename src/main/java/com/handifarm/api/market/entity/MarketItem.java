package com.handifarm.api.market.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter @Getter
@ToString(exclude = "itemImgs")
@EqualsAndHashCode()
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_market_item")
public class MarketItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long itemNo;

    @Column(nullable = false)
    private String seller;

    @Column(nullable = false)
    private String itemName;

    private String itemContent;

    @Column(nullable = false)
    private int price;

    @Column(columnDefinition = "boolean default false")
    private boolean done;

    @CreationTimestamp
    private LocalDateTime createDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "marketItem", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<ItemImg> itemImgs = new ArrayList<>();

    // 양방향 매핑에서 리스트 쪽에 데이터를 추가하는 편의 메서드 생성
    public void addItemImg(ItemImg itemImg) {
        itemImgs.add(itemImg);
        if(this != itemImg.getMarketItem()) {
            itemImg.setMarketItem(this);
        }
    }

}
