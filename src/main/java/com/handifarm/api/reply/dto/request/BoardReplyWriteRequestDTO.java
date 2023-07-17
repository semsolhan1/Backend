package com.handifarm.api.reply.dto.request;

import com.handifarm.api.board.entity.Board;
import com.handifarm.api.reply.entity.BoardReply;
import com.handifarm.jwt.TokenUserInfo;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter @Setter @ToString
@EqualsAndHashCode
@AllArgsConstructor @NoArgsConstructor
@Builder
public class BoardReplyWriteRequestDTO {

    @NotBlank
    private String reply;

    @NotBlank
    private String userNick;

    public BoardReply toEntity() {
        return BoardReply.builder()
                .reply(this.reply)
                .build();
    }

    public BoardReply toEntity(TokenUserInfo userInfo) {
        return BoardReply.builder()
                .reply(this.reply)
                .userNick(userInfo.getUserNick())
                .build();
    }



}

