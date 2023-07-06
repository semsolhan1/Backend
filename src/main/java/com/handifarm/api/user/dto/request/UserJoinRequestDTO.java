package com.handifarm.api.user.dto.request;

import com.handifarm.api.user.entity.User;
import lombok.*;

import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
@ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class UserJoinRequestDTO {

    @NotBlank
    @Size(min = 6, max = 15)
    private String userId;

    @NotBlank
    @Size(min = 8, max = 20)
    private String userPw;

    @NotNull
    private String userName;

    @Size(min = 2, max = 8)
    private String userNick;

    @NotBlank
    @Email
    private String userEmail;

    @NotBlank
    private String addrBasic;

    @NotBlank
    private String addrDetail;

//    @NotBlank
//    private String addrZipCode;

    @NotBlank
    @Size(min = 8, max = 11)
    private String userPhoneNum;

    public User dtoToEntity() {
        return User.builder()
                .userId(this.userId)
                .userPw(this.userPw)
                .userName(this.userName)
                .userNick(this.userNick)
                .userEmail(this.userEmail)
                .addrBasic(this.addrBasic)
                .addrDetail(this.addrDetail)
                .build();
    }

}
