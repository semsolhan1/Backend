package com.handifarm.api.reply.dto.response;

import com.handifarm.api.util.page.PageResponseDTO;
import lombok.*;

import java.util.List;

@Getter @Setter @ToString
@EqualsAndHashCode @AllArgsConstructor
@Builder
public class BoardReplyListResponseDTO {

    private int replyCount;

    private int totalPages;

    private PageResponseDTO pageInfo;

    private List<BoardReplyDetailResponseDTO> boardReplies;

}
