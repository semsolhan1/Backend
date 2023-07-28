package com.handifarm.api.snsBoard.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.handifarm.api.snsBoard.entity.SnsReply;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SNSReplyResponseDTO {

    private String writer;
    private String reply;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime regDate;

    public SNSReplyResponseDTO(SnsReply snsReply) {
        this.writer = snsReply.getUser().getUserNick();
        this.reply = snsReply.getReply();
        this.regDate = snsReply.getReplyTime();
    }
}
