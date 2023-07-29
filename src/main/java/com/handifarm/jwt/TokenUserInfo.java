package com.handifarm.jwt;

import lombok.*;

@Getter @Setter
@ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TokenUserInfo {

    private String userId;
    private String userNick;
    private String profileImg;

}
