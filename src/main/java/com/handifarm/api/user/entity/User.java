package com.handifarm.api.user.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Setter @Getter
@ToString @EqualsAndHashCode()
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

    private String userNick;

    @Column(nullable = false)
    private String userPhone;

    @Column(nullable = false)
    private String userEmail1;
    @Column(nullable = false)
    private String userEmail2;

    @Column(nullable = false)
    private String addrBasic;
    @Column(nullable = false)
    private String addrDetail;
    @Column(nullable = false)
    private int addrZipNum;

    @CreationTimestamp
    private LocalDateTime regDate;

}
