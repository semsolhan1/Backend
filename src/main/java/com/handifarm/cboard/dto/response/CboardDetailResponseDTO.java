package com.handifarm.cboard.dto.response;

import com.handifarm.cboard.entity.Cboard;
import lombok.*;

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

    public CboardDetailResponseDTO(Cboard cboard){
        this.id = cboard.getCboardId();
        this.title = cboard.getTitle();
        this.content = cboard.getContent();
        this.fileUp = cboard.getFileUp();
    }

}
