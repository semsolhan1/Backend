package com.handifarm.cboard.dto.response;

import com.handifarm.api.market.entity.ItemImg;
import com.handifarm.cboard.entity.BoardImg;
import com.handifarm.cboard.entity.Cboard;
import com.handifarm.cboard.entity.HashTag;
import com.handifarm.recontent.dto.response.RecontentDetailResponseDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CboardDetailResponseDTO {

    private String id;
    private String writer;
    private String content;
    private List<String> hashTags;
    private List<String> imgLinks;
    private LocalDateTime boardTime;
    private List<RecontentDetailResponseDTO> recontentDTOList;
    private int likeCount;

    public CboardDetailResponseDTO(Cboard cboard){
        this.id = cboard.getCboardId();
        this.writer = cboard.getWriter();
        this.content = cboard.getContent();
        this.boardTime = cboard.getBoardTime();

        this.hashTags = cboard.getHashTags()
                .stream()
                .map(HashTag::getHashName)
                .collect(Collectors.toList());
        this.imgLinks = getImgLinks(cboard.getItemImgs());
    }
    public CboardDetailResponseDTO(Cboard cboard, List<RecontentDetailResponseDTO> recontentDTOList) {
        this.id = cboard.getCboardId();
        this.writer = cboard.getWriter();
        this.content = cboard.getContent();
        this.boardTime = cboard.getBoardTime();
        this.hashTags = cboard.getHashTags()
                .stream()
                .map(HashTag::getHashName)
                .collect(Collectors.toList());
        this.recontentDTOList = recontentDTOList.stream()
                .sorted(Comparator.comparing(RecontentDetailResponseDTO::getRecontentOrder))
                .collect(Collectors.toList());
        this.imgLinks = getImgLinks(cboard.getItemImgs());
    }

    private List<String> getImgLinks(List<BoardImg> itemImgs) {

        return itemImgs.stream()
                .map(BoardImg::getImgLink)
                .collect(Collectors.toList());
    }

}
