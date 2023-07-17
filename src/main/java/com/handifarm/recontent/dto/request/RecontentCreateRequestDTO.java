package com.handifarm.recontent.dto.request;

import com.handifarm.cboard.entity.Cboard;
import com.handifarm.recontent.entity.Recontent;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class RecontentCreateRequestDTO {


    @NotBlank
    private String rewriter;

    @NotBlank
    private String recontent;

    private LocalDateTime recontentTime;

    private Cboard cboardId;

//    private String cboard;

    public Recontent toEntity(Cboard cboard) {
        return Recontent.builder()
                .recontent(this.recontent)
                .rewriter(this.rewriter)
                .recontentTime(this.recontentTime)
                .cboard(cboard)
                .build();


    }
}
