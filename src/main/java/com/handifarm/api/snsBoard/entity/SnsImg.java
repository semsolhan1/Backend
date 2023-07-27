package com.handifarm.api.snsBoard.entity;

import lombok.*;

import javax.persistence.*;

@Getter @Setter
@ToString()
@EqualsAndHashCode(of = "snsImgNo")
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_sns_img")
public class SnsImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long snsImgNo;

    @Column(nullable = false)
    private String snsImgLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "snsNo")
    private SnsBoard snsBoard;

}
