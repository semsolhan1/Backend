package com.handifarm.recontent.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    private int recontentOrder;
    private String rewriter;
    private String recontent;
    private LocalDateTime recontentTime;

    @JsonIgnore
    private CboardDetailResponseDTO cboard;

    private int likeCount;

    public RecontentDetailResponseDTO(Recontent recontent){
        this.recontentOrder = recontent.getRecontentOrder();
        this.rewriter = recontent.getRewriter();
        this.recontent = recontent.getRecontent();
        this.recontentTime = recontent.getRecontentTime();
        this.cboard = new CboardDetailResponseDTO(recontent.getCboard());
    }

}
