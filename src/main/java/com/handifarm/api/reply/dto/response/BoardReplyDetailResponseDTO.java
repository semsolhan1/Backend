package com.handifarm.api.reply.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.handifarm.api.reply.entity.BoardReply;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @ToString
@EqualsAndHashCode @AllArgsConstructor @NoArgsConstructor
@Builder
public class BoardReplyDetailResponseDTO {

    private Long boardNo;
    private Long replyNo;
    private String reply;
    private String userNick;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    private LocalDateTime createDate;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    private LocalDateTime updateDate;

    public BoardReplyDetailResponseDTO(BoardReply boardReply) {
        this.boardNo = boardReply.getBoardNo();
        this.replyNo = boardReply.getReplyNo();
        this.userNick = boardReply.getUserNick();
        this.reply = boardReply.getReply();
        this.createDate = boardReply.getCreateDate();
        this.updateDate = boardReply.getUpdateDate();
    }

}



