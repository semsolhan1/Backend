package com.handifarm.cboard.dto.response;

import com.handifarm.cboard.entity.Cboard;
import com.handifarm.cboard.entity.HashTag;
import com.handifarm.recontent.dto.response.RecontentDetailResponseDTO;
import lombok.*;

import java.time.LocalDateTime;
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
    private String title;
    private String content;
    private String fileUp;
    private List<String> hashTags;
    private LocalDateTime boardTime;
    private List<RecontentDetailResponseDTO> recontentDTOList;

    public CboardDetailResponseDTO(Cboard cboard){
        this.id = cboard.getCboardId();
        this.writer = cboard.getWriter();
        this.title = cboard.getTitle();
        this.content = cboard.getContent();
        this.fileUp = cboard.getFileUp();
        this.boardTime = cboard.getBoardTime();

        this.hashTags = cboard.getHashTags()
                .stream()
                .map(HashTag::getHashName)
                .collect(Collectors.toList());
    }
    public CboardDetailResponseDTO(Cboard cboard, List<RecontentDetailResponseDTO> recontentDTOList) {
        this.id = cboard.getCboardId();
        this.writer = cboard.getWriter();
        this.title = cboard.getTitle();
        this.content = cboard.getContent();
        this.fileUp = cboard.getFileUp();
        this.boardTime = cboard.getBoardTime();
        this.hashTags = cboard.getHashTags()
                .stream()
                .map(HashTag::getHashName)
                .collect(Collectors.toList());
        this.recontentDTOList = recontentDTOList;
    }

}
