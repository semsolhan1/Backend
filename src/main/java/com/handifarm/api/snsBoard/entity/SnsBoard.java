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

    @Column(nullable = false)
    private String writer;

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
    private List<SnsLike> likes = new ArrayList<>();

}
