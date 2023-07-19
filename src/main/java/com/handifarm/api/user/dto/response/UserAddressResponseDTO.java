package com.handifarm.api.user.dto.response;

import lombok.*;

@Getter
@ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class UserAddressResponseDTO {

    private String addrBasic;
    private String addrDetail;
    private int addrZipCode;

}
