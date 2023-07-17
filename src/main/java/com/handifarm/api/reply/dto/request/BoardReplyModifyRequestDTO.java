package com.handifarm.api.reply.dto.request;

import com.handifarm.api.reply.entity.BoardReply;
import com.handifarm.jwt.TokenUserInfo;
import lombok.*;

import javax.swing.*;
import javax.validation.constraints.NotBlank;

@Getter @Setter @ToString
@EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
public class BoardReplyModifyRequestDTO {

    private Long boardNo;

    private Long boardReplyNo;

    @NotBlank
    private String reply;

    public BoardReply toEntity(TokenUserInfo userInfo) {
        return BoardReply.builder()
                .reply(this.reply)
                .userNick(userInfo.getUserNick())
                .build();
    }

}
