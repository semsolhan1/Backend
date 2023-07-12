package com.handifarm.api.market.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Setter @Getter
@ToString @EqualsAndHashCode()
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_marketItem")
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

    @Column(columnDefinition = "boolean default 'false'")
    private boolean done;

    @CreationTimestamp
    private LocalDateTime createDate;

    @OneToMany(mappedBy = "marketItem", cascade = CascadeType.REMOVE)
    private List<ItemImg> itemImgs;

}
