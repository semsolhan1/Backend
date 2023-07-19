package com.handifarm.recontent.dto.request;

import com.handifarm.cboard.entity.Cboard;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecontentModifyRequestDTO {

    @NotBlank
    private String rewriter;

    private String recontent;

    private LocalDateTime recontentTime;

    private Cboard cboardId;
}
