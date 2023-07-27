package com.handifarm.api.snsBoard.entity;

import com.handifarm.api.user.entity.User;
import lombok.*;

import javax.persistence.*;

@Getter @Setter
@ToString()
@EqualsAndHashCode()
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_sns_like")
public class SnsLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long likeNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "snsNo")
    private SnsBoard snsBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userNick")
    private User user;

}
