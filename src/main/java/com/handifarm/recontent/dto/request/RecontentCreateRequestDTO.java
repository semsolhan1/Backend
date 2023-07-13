package com.handifarm.recontent.dto.request;

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

    public Recontent toEntity() {
        return Recontent.builder()
                .recontent(this.recontent)
                .rewriter(this.rewriter)
                .recontentTime(this.recontentTime)
                .build();
    }
}
