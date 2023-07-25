package com.handifarm.api.user.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.handifarm.api.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class UserInfoResponseDTO {

    private String userName;
    private String userNick;
    private String userEmail;
    private String userPhone;
    private String userAddrBasic;
    private String userAddrDetail;
    private int userPostcode;
    private String userProfileImg;
    @JsonFormat(pattern = "yyyy년 MM월 dd일")
    private LocalDateTime joinDate;

    public UserInfoResponseDTO(User user) {
        this.userName = user.getUserName();
        this.userNick = user.getUserNick();
        this.userEmail = user.getUserEmail();
        this.userPhone = user.getUserPhone();
        this.userAddrBasic = user.getUserAddrBasic();
        this.userAddrDetail = user.getUserAddrDetail();
        this.userPostcode = user.getUserPostcode();
        this.userProfileImg = user.getUserProfileImg();
        this.joinDate = user.getJoinDate();
    }
}
