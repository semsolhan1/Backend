package com.handifarm.api.user.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class UserInfoResponseDTO {

    private String userEmail;
    private String userPhoneNum;
    @JsonFormat(pattern = "yyyy년 MM월 dd일")
    private LocalDateTime joinDate;

}
