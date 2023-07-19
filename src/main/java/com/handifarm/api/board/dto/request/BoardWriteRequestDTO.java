package com.handifarm.api.board.dto.request;

import com.handifarm.api.board.entity.Board;
import com.handifarm.jwt.TokenUserInfo;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter @ToString
@EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class BoardWriteRequestDTO {

    @NotNull
    private String category;

    @NotBlank
    @Size(min=8, max=20)
    private String title;

    @NotBlank
    private String content;

    private String userNick;

    public Board toEntity() {

        return Board.builder()
                .title(this.title)
                .content(this.content)
                .category(Board.Category.valueOf(this.category))
                .build();
    }

    public Board toEntity(TokenUserInfo userInfo) {
        return Board.builder()
                .title(this.title)
                .content(this.content)
                .category(Board.Category.valueOf(this.category))
                .userNick(userInfo.getUserNick())
                .build();
    }

}


