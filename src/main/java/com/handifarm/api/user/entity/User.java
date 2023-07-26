package com.handifarm.api.user.entity;

import com.handifarm.api.board.entity.Board;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter @Getter
@ToString @EqualsAndHashCode(of = "userId")
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_users")
public class User {

    @Id
    private String userId;

    @Column(nullable = false)
    private String userPw;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false, unique = true)
    private String userNick;

    @Column(nullable = false)
    private String userPhone;

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private String userAddrBasic;
    @Column(nullable = false)
    private String userAddrDetail;
    @Column(nullable = false)
    private int userPostcode;

    private String userProfileImg;

    @CreationTimestamp
    private LocalDateTime joinDate;

}
