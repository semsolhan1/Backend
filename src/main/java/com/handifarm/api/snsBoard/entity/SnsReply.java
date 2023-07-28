package com.handifarm.api.snsBoard.entity;

import com.handifarm.api.user.entity.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Setter
@ToString()
@EqualsAndHashCode()
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_sns_reply")
public class SnsReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sns_reply_no")
    private long replyNo;

    @Column(name = "sns_reply", nullable = false)
    private String reply;

    @CreationTimestamp
    private LocalDateTime replyTime;

    @ManyToOne
    @JoinColumn(name = "snsNo")
    private SnsBoard snsBoard;

    @ManyToOne
    @JoinColumn(name = "sns_reply_writer", referencedColumnName = "userNick")
    private User user;

}
