package com.handifarm.api.user.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String token;

}
