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
    private Long boardNo;

    public BoardReply toEntity() {
        return BoardReply.builder().boardNo(this.boardNo).boardReplyContent(this.boardReplyContent).build();
    }


}
