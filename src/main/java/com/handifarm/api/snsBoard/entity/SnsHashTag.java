package com.handifarm.api.snsBoard.entity;

import lombok.*;

import javax.persistence.*;

@Getter @Setter
@ToString()
@EqualsAndHashCode(of = "hashTagNo")
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_sns_hash_tag")
public class SnsHashTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long hashTagNo;

    private String hashTag;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private SnsBoard snsBoard;

}
