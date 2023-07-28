package com.handifarm.api.snsBoard.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.handifarm.api.snsBoard.entity.SnsBoard;
import com.handifarm.api.snsBoard.entity.SnsHashTag;
import com.handifarm.api.snsBoard.entity.SnsImg;
import com.handifarm.api.snsBoard.entity.SnsReply;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SNSBoardResponseDTO {

    private String content;
    private String writer;
    private List<String> hashTags;
    private List<String> snsImgs;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime regDate;
    private List<SNSReplyResponseDTO> replyList;
    private long likeCount;

    // 엔터티를 DTO로 변환
    public SNSBoardResponseDTO(SnsBoard snsBoard) {
        this.content = snsBoard.getContent();
        this.writer = snsBoard.getWriter();
        this.hashTags = getHashTagList(snsBoard.getHashTags());
        this.snsImgs = getSnsImgLinks(snsBoard.getSnsImgs());
        this.regDate = snsBoard.getUploadTime();
        this.replyList = getReplys(snsBoard.getReplyList());
        // 엔티티에 존재하는 like의 크기를 반환 (좋아요 수 계산)
        this.likeCount = snsBoard.getLikes().size();
    }

    // SnsHashTag List를 String List로 변환하는 메서드
    private List<String> getHashTagList(List<SnsHashTag> hashTags) {
        return hashTags.stream()
                .map(SnsHashTag::getHashTag)
                .collect(Collectors.toList());
    }

    private List<String> getSnsImgLinks(List<SnsImg> snsImgs) {
        return snsImgs.stream()
                .map(SnsImg::getSnsImgLink)
                .collect(Collectors.toList());
    }

    private List<SNSReplyResponseDTO> getReplys(List<SnsReply> replyList) {
        return replyList.stream()
                .map(SNSReplyResponseDTO::new)
                .collect(Collectors.toList());
    }

}
