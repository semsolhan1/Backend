package com.handifarm.api.snsBoard.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SNSBoardDetailResponseDTO {

    private String content;
    private String writer;
    private List<String> hashTags;
    private List<String> snsImgs;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime regDate;
    private List<SNSReplyResponseDTO> replyList;
    private long likeCount;

}
