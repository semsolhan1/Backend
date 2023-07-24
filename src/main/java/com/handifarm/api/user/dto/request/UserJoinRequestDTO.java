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

    @NotBlank
    private String userName;

    @Size(max = 15)
    private String userNick;

    @NotBlank
    @Email
    private String userEmail;

    @NotBlank
    @Size(min = 8, max = 11)
    private String userPhone;

    @NotBlank
    private String userAddrBasic;

    @NotBlank
    private String userAddrDetail;

    @NotNull
    private int userPostcode;

    public User dtoToEntity() {
        return User.builder()
                .userId(this.userId)
                .userPw(this.userPw)
                .userName(this.userName)
                .userNick(this.userNick != null ? this.userNick : this.userId)
                .userEmail(this.userEmail)
                .userPhoneNum(this.userPhone)
                .addrBasic(this.userAddrBasic)
                .addrDetail(this.userAddrDetail)
                .addrZipCode(this.userPostcode)
                .build();
    }

}
