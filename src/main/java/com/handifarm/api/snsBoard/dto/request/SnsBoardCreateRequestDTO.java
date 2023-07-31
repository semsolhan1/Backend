package com.handifarm.api.snsBoard.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter @Setter
@ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SnsBoardCreateRequestDTO {

    @NotBlank
    private String content;

    private List<String> hashTags;

}
