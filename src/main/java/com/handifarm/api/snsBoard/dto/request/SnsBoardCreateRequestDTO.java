package com.handifarm.api.snsBoard.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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

    private List<MultipartFile> snsImgs;

}
