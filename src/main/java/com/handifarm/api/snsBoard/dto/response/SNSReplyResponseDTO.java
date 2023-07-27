package com.handifarm.api.snsBoard.dto.response;

import lombok.*;

@Getter @Setter
@ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SNSReplyResponseDTO {

    String writer;
    String reply;

}
