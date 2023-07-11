package com.handifarm.cboard.dto.response;

import com.handifarm.cboard.entity.Cboard;
import com.handifarm.cboard.entity.HashTag;
import lombok.*;

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
    private String title;
    private String content;
    private String fileUp;
    private List<String> hashTags;


    public CboardDetailResponseDTO(Cboard cboard){
        this.id = cboard.getCboardId();
        this.title = cboard.getTitle();
        this.content = cboard.getContent();
        this.fileUp = cboard.getFileUp();

        this.hashTags = cboard.getHashTags()
                .stream()
                .map(HashTag::getHashName)
                .collect(Collectors.toList());
    }

}
