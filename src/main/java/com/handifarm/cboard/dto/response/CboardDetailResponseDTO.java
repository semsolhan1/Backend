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

    public CboardDetailResponseDTO(Cboard cboard){
        this.id = cboard.getCboardId();
        this.title = cboard.getTitle();
    }

}
