package com.handifarm.api.board.dto.request;

import com.handifarm.api.board.entity.Board;
import com.handifarm.api.board.entity.BoardReply;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardReplyWriteRequestDTO {

    @NotNull
    private String boardReplyContent;

    @NotNull
    private String userNick;

    public BoardReply toEntity() {
        return BoardReply.builder().boardReplyContent(this.boardReplyContent).userNick(this.userNick).build();
    }

}
