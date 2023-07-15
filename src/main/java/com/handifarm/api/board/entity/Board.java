package com.handifarm.api.board.entity;

import com.handifarm.api.user.entity.User;
import jdk.jfr.Category;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter @Getter @ToString
@EqualsAndHashCode @NoArgsConstructor @AllArgsConstructor
@Builder @Entity
@Table(name = "tbl_board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardNo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int views;

    @CreationTimestamp
    private LocalDateTime createDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_nick")
    private User user;

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<BoardReply> replies = new ArrayList<>();

    // 양방향 매핑에서 리스트쪽에 데이터를 추가하는 편의메서드 생성
    public void addReply(BoardReply reply) {
        replies.add(reply);
        if(this != reply.getBoard()) {
            reply.setBoard(this);
        }
    }

    public enum Category {
        NOTICE,
        FREE,
        INFORMATION
    };

}
