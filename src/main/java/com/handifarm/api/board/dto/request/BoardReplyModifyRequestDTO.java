package com.handifarm.api.board.dto.request;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardReplyModifyRequestDTO {

    private Long boardReplyNo;

    private String BoardReplyContent;

    private String userNick;


}
