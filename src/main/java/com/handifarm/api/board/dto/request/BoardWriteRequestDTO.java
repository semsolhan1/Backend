package com.handifarm.api.board.dto.request;

import com.handifarm.api.board.entity.Board;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter @ToString
@EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class BoardWriteRequestDTO {

    @NotNull
    private String category;

    @NotNull
    @Size(min=8, max=20)
    private String title;

    @NotNull
    private String content;

    public Board toEntity() {
        return Board.builder().title(this.title).content(this.content).category(Board.Category.valueOf(this.category)).build();
    }

}


