package com.handifarm.api.user.dto.request;

import com.handifarm.api.user.entity.User;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
@ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class UserInfoModifyRequestDTO {

    @NotBlank
    @Size(min = 6, max = 15)
    private String userId;

    @Size(max = 20)
    private String userPw;

    @NotBlank
    private String userName;

    @NotBlank
    private String userNick;

    @NotBlank
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

    private String userProfileImg;

}
