package com.handifarm.api.user.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.handifarm.api.user.entity.User;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class UserLoginResponseDTO {

    private String userId;
    private String userName;
    private String userNick;
    private String userProfileImg;
    private String token;

    public UserLoginResponseDTO(User user, String token) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.userNick = user.getUserNick();
        this.userProfileImg = user.getUserProfileImg();
        this.token = token;
    }

}
