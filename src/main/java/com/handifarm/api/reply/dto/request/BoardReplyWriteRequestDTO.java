package com.handifarm.api.reply.dto.request;

import com.handifarm.api.board.entity.Board;
import com.handifarm.api.reply.entity.BoardReply;
import com.handifarm.jwt.TokenUserInfo;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter @ToString
@EqualsAndHashCode
@AllArgsConstructor @NoArgsConstructor
@Builder
public class BoardReplyWriteRequestDTO {

    @NotBlank
    private String reply;

    @NotNull
    private Long replyNo;

    @NotBlank
    private String userNick;

    public BoardReply toEntity(TokenUserInfo userInfo, Long boardNo) {
        return BoardReply.builder()
                .boardNo(boardNo)
                .reply(this.reply)
                .userNick(userInfo.getUserNick())
                .build();
    }



}

