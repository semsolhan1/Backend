package com.handifarm.api.user.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginRequestDTO {

    @NotBlank
    private String userId;

    @NotBlank
    private String userPw;

}
