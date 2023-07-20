package com.handifarm.cboard.entity;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@ToString(exclude = "cboard")
@EqualsAndHashCode()
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_board_img")
public class BoardImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long imgNo;

    @Column(nullable = false)
    private String imgLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cboardId")
    private Cboard cboard;

}
