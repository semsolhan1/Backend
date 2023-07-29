package com.handifarm.api.snsBoard.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@ToString()
@EqualsAndHashCode(of = "snsNo")
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_sns_board")
public class SnsBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long snsNo;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false, name = "writer")
    private String userNick;

    @CreationTimestamp
    private LocalDateTime uploadTime;

    @OneToMany(mappedBy = "snsBoard", cascade = CascadeType.ALL)
    @Builder.Default
    private List<SnsHashTag> hashTags = new ArrayList<>();

    @OneToMany(mappedBy = "snsBoard", cascade = CascadeType.ALL)
    @Builder.Default
    private List<SnsReply> replyList = new ArrayList<>();

    @OneToMany(mappedBy = "snsBoard", cascade = CascadeType.ALL)
    @Builder.Default
    private List<SnsImg> snsImgs = new ArrayList<>();

    @OneToMany(mappedBy = "snsBoard", cascade = CascadeType.ALL)
    @Builder.Default
    private List<SnsLike> likes = new ArrayList<>();

    // SNS Img 추가 편의 메서드
    public void addSnsImg(SnsImg snsImg) {
        snsImgs.add(snsImg);
        if(this != snsImg.getSnsBoard()) {
            snsImg.setSnsBoard(this);
        }
    }

    // SNS HashTag 추가 편의 메서드
    public void addHashTag(SnsHashTag hashTag) {
        hashTags.add(hashTag);
        if(this != hashTag.getSnsBoard()) {
            hashTag.setSnsBoard(this);
        }
    }

}
