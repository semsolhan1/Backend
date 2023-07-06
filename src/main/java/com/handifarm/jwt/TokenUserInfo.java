package com.handifarm.jwt;

import lombok.*;

@Getter
@ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TokenUserInfo {

    private String userId;

}
