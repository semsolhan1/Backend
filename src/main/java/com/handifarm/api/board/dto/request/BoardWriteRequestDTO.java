package com.handifarm.api.board.dto.request;

import jdk.jfr.Category;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardWriteRequestDTO {


    private String userId;

    private Category category;

    @NotNull
    @Size(min=8, max=20)
    private String title;

    @NotNull
    private String content;

}
