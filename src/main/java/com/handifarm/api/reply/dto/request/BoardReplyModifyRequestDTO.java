package com.handifarm.api.reply.dto.request;

import com.handifarm.api.reply.entity.BoardReply;
import com.handifarm.jwt.TokenUserInfo;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter @Setter @ToString
@EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
public class BoardReplyModifyRequestDTO {

    private Long replyNo;

    @NotBlank
    private String reply;

    public BoardReply toEntity(TokenUserInfo userInfo) {
        // 기존의 댓글 엔티티를 수정하는 경우이므로 replyNo를 사용하여 해당 댓글을 식별하고,
        // reply 속성만 업데이트하여 BoardReply 엔티티를 반환합니다.
        return BoardReply.builder()
                .replyNo(this.replyNo)
                .reply(this.reply)
                .build();
    }

}



