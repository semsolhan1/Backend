package com.handifarm.api.board.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter @ToString
@EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class BoardModifyRequestDTO {

    private Long boardNo;

    @NotNull
    @Size(min=8, max=20)
    private String title;

    @NotNull
    private String content;

}
