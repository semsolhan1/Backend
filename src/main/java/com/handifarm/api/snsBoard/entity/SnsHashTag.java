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
    @Column(name = "sns_hash_tag_no")
    private long hashTagNo;

    @Column(name = "sns_hash_tag", nullable = false)
    private String hashTag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "snsNo")
    private SnsBoard snsBoard;

}
