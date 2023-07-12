package com.handifarm.recontent.dto.response;

import com.handifarm.cboard.dto.response.CboardDetailResponseDTO;
import com.handifarm.recontent.entity.Recontent;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecontentDetailResponseDTO {

    private String id;
    private String writer;
    private String content;
    private LocalDateTime recontentTime;
    private CboardDetailResponseDTO cboard;

    public RecontentDetailResponseDTO(Recontent recontent){
        this.id = recontent.getRecontentId();
        this.writer = recontent.getRewriter();
        this.content = recontent.getRecontent();
        this.recontentTime = recontent.getRecontentTime();
        this.cboard = new CboardDetailResponseDTO(recontent.getCboard());

    }

}
