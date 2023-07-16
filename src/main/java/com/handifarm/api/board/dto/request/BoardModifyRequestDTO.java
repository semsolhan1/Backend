package com.handifarm.api.board.dto.request;

import com.handifarm.api.board.entity.Board;
import com.handifarm.jwt.TokenUserInfo;
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

    @NotNull
    private Board.Category category;

    public Board toEntity(TokenUserInfo userInfo) {
        return Board.builder()
                .category(this.category)
                .title(this.title)
                .content(this.content)
                .userNick(userInfo.getUserNick())
                .build();
    }



}
