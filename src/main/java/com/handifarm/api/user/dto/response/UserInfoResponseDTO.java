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
    private String userEmail;
    private String userPhone;
    private String userAddrBasic;
    private String userAddrDetail;
    private String userNick;
    private int userPostcode;
    @JsonFormat(pattern = "yyyy년 MM월 dd일")
    private LocalDateTime joinDate;

    public UserInfoResponseDTO(User user) {
        this.userName = user.getUserName();
        this.userEmail = user.getUserEmail();
        this.userPhone = user.getUserPhoneNum();
        this.userAddrBasic = user.getAddrBasic();
        this.userAddrDetail = user.getAddrDetail();
        this.userPostcode = user.getAddrZipCode();
        this.userNick = user.getUserNick();
        this.joinDate = user.getJoinDate();
    }
}
